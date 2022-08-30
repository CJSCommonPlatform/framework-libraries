package uk.gov.justice.services.test.utils.core.messaging;

import static java.lang.String.format;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class JmsSessionFactory implements AutoCloseable {

    private final ActiveMQConnectionFactory activeMQConnectionFactory;

    private Session session;
    private Connection connection;

    public JmsSessionFactory() {
        this(new ActiveMQConnectionFactory());

    }
    
    public JmsSessionFactory(final ActiveMQConnectionFactory activeMQConnectionFactory) {
        this.activeMQConnectionFactory = activeMQConnectionFactory;
    }

    public Session session(final String queueUri) {

        try {
            activeMQConnectionFactory.setBrokerURL(queueUri);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, AUTO_ACKNOWLEDGE);
            return session;
        } catch (final JMSException e) {
            throw new MessageConsumerException(format("Failed to create JMS session for queue uri '%s'", queueUri), e);
        }
    }

    @Override
    public void close() {
        doClose(session);
        doClose(connection);
        doClose(activeMQConnectionFactory);
    }

    private void doClose(final AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception ignored) {
                // do nothing
            }
        }
    }
}
