package uk.gov.justice.artemis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.jms.server.config.ConnectionFactoryConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSQueueConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.junit.jupiter.api.Test;

public class EmbeddedArtemisServerIT {


    @Test
    public void shouldTestServerFlow() {

        try {
            String queueName = "testQueue";

            EmbeddedJMS jmsServer = new EmbeddedJMS().setConfiguration(getConfiguration())
                            .setJmsConfiguration(getJMSConfiguration(queueName));

            EmbeddedArtemisServer.setEmbeddedJms(jmsServer);

            EmbeddedArtemisServer.startServer();

            ConnectionFactory cf = (ConnectionFactory) jmsServer.lookup("cf");

            Queue queue = (Queue) jmsServer.lookup(String.join("/", "queue", queueName));

            try (Connection connection = cf.createConnection()) {
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(queue);
                TextMessage message = session.createTextMessage("Hello sent at " + new Date());
                String expected = message.getText();
                producer.send(message);
                MessageConsumer messageConsumer = session.createConsumer(queue);
                connection.start();
                TextMessage messageReceived = (TextMessage) messageConsumer.receive(1000);

                assertEquals(expected, messageReceived.getText());
            }
        } catch (Exception e) {

            fail();
        } finally {
            EmbeddedArtemisServer.stopServer();
        }
    }

    private Configuration getConfiguration() {

        return new ConfigurationImpl().setPersistenceEnabled(false)
                        .setJournalDirectory("target/data/journal").setSecurityEnabled(false)
                        .addAcceptorConfiguration(new TransportConfiguration(
                                        NettyAcceptorFactory.class.getName()))
                        .addConnectorConfiguration("connector", new TransportConfiguration(
                                        NettyConnectorFactory.class.getName()));

    }

    private JMSConfiguration getJMSConfiguration(String queueName) {

        JMSConfiguration jmsConfig = new JMSConfigurationImpl();

        ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl()
                        .setName("cf").setConnectorNames(Arrays.asList("connector"))
                        .setBindings("cf");
        jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

        JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl().setName(queueName)
                        .setDurable(false).setBindings(String.join("/", "queue", queueName));
        jmsConfig.getQueueConfigurations().add(queueConfig);

        return jmsConfig;
    }
}
