package uk.gov.justice.services.test.utils.core.messaging;

import java.util.HashSet;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import com.google.common.annotations.VisibleForTesting;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;

public class MessageConsumerFactory {

    private Session session;
    private final Set<MessageConsumer> messageConsumers = new HashSet<>();
    private final JmsSessionFactory jmsSessionFactory;
    private final TopicFactory topicFactory;

    public MessageConsumerFactory(final JmsSessionFactory jmsSessionFactory) {
        this(jmsSessionFactory, new TopicFactory());
    }

    @VisibleForTesting
    public MessageConsumerFactory(final JmsSessionFactory jmsSessionFactory, final TopicFactory topicFactory) {
        this.jmsSessionFactory = jmsSessionFactory;
        this.topicFactory = topicFactory;
    }

    public MessageConsumer createAndStart(
            final String messageSelector,
            final String queueUri,
            final String topicName) throws JMSException {

        if (session == null) {
            session = jmsSessionFactory.session(queueUri);
        }

        final ActiveMQTopic topic = topicFactory.createTopic(topicName);
        final MessageConsumer messageConsumer = session.createConsumer(
                topic,
                messageSelector);

        messageConsumers.add(messageConsumer);

        return messageConsumer;
    }

    public void close() {
        doClose(session);
        doClose(jmsSessionFactory);
        messageConsumers.forEach(this::doClose);
        messageConsumers.clear();
        session = null;
    }

    private void doClose(final AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception ignored) {
            }
        }
    }
}
