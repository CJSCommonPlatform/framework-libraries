package uk.gov.justice.services.test.utils.core.messaging;

import static java.util.stream.Collectors.toCollection;
import static uk.gov.justice.services.test.utils.core.messaging.JsonObjects.jsonReaderFactory;
import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.artemisQueueUri;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class that allows to browse and clean messages in dead letter queue
 * <p>
 * Usage: DeadLetterQueueBrowser dlqBrowser = new DeadLetterQueueBrowser(); dlqBrowser.browse() will
 * return a list of {@link String} in the dlq dlqBrowser.removeMessages() will clean dlq
 * dlqBrowser.close() will release resources
 * <p>
 * Note:It has been observed there is sometimes a delay by the time the message lands in dlq.
 * Setting a delay of few milliseconds generally resolves this.
 *
 * @author gopal
 */
public class DeadLetterQueueBrowser implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterQueueBrowser.class);

    private static final List<String> DLQ_QUEUE_URIS = artemisQueueUri();
    private static final String DEFAULT_DLQ_NAME = "jms.queue.DLQ";

    private final List<Session> sessions;
    private final List<JmsSessionFactory> jmsSessionFactories;
    private Queue dlqQueue;
    private final ConsumerClient consumerClient;

    private final String dlqName;

    /**
     * Creates a browser for the Dead Letter Queue with the default name of 'jms.queue.DLQ'
     */
    public DeadLetterQueueBrowser() {
        this(DEFAULT_DLQ_NAME);
    }

    /**
     * Creates a browser for the Dead Letter Queue with the supplied name
     *
     * @param dlqName The name of the DLQ
     */
    public DeadLetterQueueBrowser(final String dlqName) {
        this.dlqName = dlqName;
        consumerClient = new ConsumerClient();
        jmsSessionFactories = Lists.newArrayList();
        sessions = Lists.newArrayList();
        initialise();
    }

    @VisibleForTesting
    DeadLetterQueueBrowser(
            final String dlqName,
            final Queue dlqQueue,
            final List<Session> sessions,
            final List<JmsSessionFactory> jmsSessionFactories,
            final ConsumerClient consumerClient) {
        super();
        this.dlqName = dlqName;
        this.sessions = sessions;
        this.jmsSessionFactories = jmsSessionFactories;
        this.dlqQueue = dlqQueue;
        this.consumerClient = consumerClient;
    }

    private void initialise() {
        try {
            DLQ_QUEUE_URIS.forEach(u -> {
                LOGGER.info("Setting up session for uri: " + u);
                final JmsSessionFactory jmsSessionFactory = new JmsSessionFactory();
                sessions.add(jmsSessionFactory.session(u));
                jmsSessionFactories.add(jmsSessionFactory);
            });
            dlqQueue = new ActiveMQQueue(dlqName);
        } catch (Exception e) {
            close();
            final String message = "Failed to start dlq message consumer for " + "queue: '" + dlqName + "', "
                    + "queueUris: '" + DLQ_QUEUE_URIS + " ";
            LOGGER.error("Fatal error initialising Artemis {}  ", message);
            throw new MessageConsumerException(message, e);
        }
    }

    /**
     * allows browsing messages in dlq
     *
     * @return list of {@link JsonObject}
     */
    public List<JsonObject> browseAsJson() {
        return browse().stream().map(this::convert).collect(toCollection(ArrayList::new));
    }

    /**
     * allows browsing messages in dlq
     *
     * @return list of {@link String}
     */
    public List<String> browse() {

        final List<String> messages = new ArrayList<>();
        for (Session session : sessions) {
            try (QueueBrowser dlqBrowser = session.createBrowser(dlqQueue);) {
                final Enumeration enumeration = dlqBrowser.getEnumeration();

                while (enumeration.hasMoreElements()) {
                    final String message = ((TextMessage) enumeration.nextElement()).getText();
                    messages.add(message);
                }


            } catch (JMSException e) {
                final String message = "Fatal error getting messages from DLQ";
                LOGGER.error(message);
                throw new MessageConsumerException(message, e);
            }
        }
        LOGGER.info("Total number of messages across {} brokers is {}", sessions.size(), messages.size());
        return messages;
    }

    /**
     * removes messages from dlq
     */
    public void removeMessages() {
        for (Session session : sessions) {
            try (MessageConsumer messageConsumer = session.createConsumer(dlqQueue)) {
                consumerClient.cleanQueue(messageConsumer);
            } catch (JMSException e) {
                final String message = "Fatal error cleaning messges from DLQ";
                LOGGER.error(message);
                throw new MessageConsumerException(message, e);
            }
        }
    }

    private JsonObject convert(final String source) {
        try (final JsonReader reader = jsonReaderFactory.createReader(new StringReader(source))) {
            return reader.readObject();
        }
    }

    /**
     * clean up resources
     */
    public void close() {
        for (JmsSessionFactory jmsSessionFactory : jmsSessionFactories) {
            jmsSessionFactory.close();
        }
    }
}
