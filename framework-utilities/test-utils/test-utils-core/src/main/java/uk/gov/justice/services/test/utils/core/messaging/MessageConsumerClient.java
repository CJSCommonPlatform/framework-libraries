package uk.gov.justice.services.test.utils.core.messaging;

import static java.lang.String.format;
import static uk.gov.justice.services.test.utils.core.messaging.QueueUriProvider.queueUri;

import java.util.Optional;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import com.google.common.annotations.VisibleForTesting;

public class MessageConsumerClient  implements AutoCloseable {

    public static final long TIMEOUT_IN_MILLIS = 20_000;
    public static final String QUEUE_URI = queueUri();

    private static final String MESSAGE_SELECTOR_TEMPLATE = "CPPNAME IN ('%s')";


    private final MessageConsumerFactory messageConsumerFactory;

    private MessageConsumer messageConsumer;
    private ConsumerClient consumerClient;

    @Deprecated(since = "Please use MessageConsumerClientBuilder to create instead of using this constructor")
    public MessageConsumerClient() {
        this(new JmsSessionFactory());
    }


    MessageConsumerClient(final JmsSessionFactory jmsSessionFactory) {
        this(new MessageConsumerFactory(jmsSessionFactory), new ConsumerClient());
    }

    @VisibleForTesting
    MessageConsumerClient(final MessageConsumerFactory messageConsumerFactory, final ConsumerClient consumerClient) {
        this.messageConsumerFactory = messageConsumerFactory;
        this.consumerClient = consumerClient;
    }
    public void startConsumer(final String eventName, final String topicName, final int port) {

        startConsumer(eventName, topicName, queueUri(port));
    }

    public void startConsumer(final String eventName, final String topicName) {

        startConsumer(eventName, topicName, QUEUE_URI);
    }

    public void startConsumer(final String eventName, final String topicName, final String queueURI) {

        final String messageSelector = format(MESSAGE_SELECTOR_TEMPLATE, eventName);

        try {
            messageConsumer = messageConsumerFactory.createAndStart(
                    messageSelector,
                    queueURI,
                    topicName);

        } catch (final JMSException e) {
            close();
            final String message = "Failed to start message consumer for " +
                    "eventName '" + eventName + "':, " +
                    "topic: '" + topicName + "', " +
                    "queueUri: '" + queueURI + "', " +
                    "messageSelector: '" + messageSelector + "'";
            throw new MessageConsumerException(message, e);
        }
    }

    public Optional<String> retrieveMessageNoWait() {
        return consumerClient.retrieveMessageNoWait(messageConsumer);
    }

    public Optional<String> retrieveMessage() {
        return consumerClient.retrieveMessage(messageConsumer,TIMEOUT_IN_MILLIS);
    }

    public Optional<String> retrieveMessage(final long timeout) {
        return consumerClient.retrieveMessage(messageConsumer,timeout);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void cleanQueue() {
        consumerClient.cleanQueue(messageConsumer);
    }

    @Override
    public void close() {
        messageConsumerFactory.close();
    }


    @VisibleForTesting
    MessageConsumer getMessageConsumer() {
        return messageConsumer;
    }
}
