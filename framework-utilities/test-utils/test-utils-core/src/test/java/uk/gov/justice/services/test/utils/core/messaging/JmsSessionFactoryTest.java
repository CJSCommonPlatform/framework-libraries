package uk.gov.justice.services.test.utils.core.messaging;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JmsSessionFactoryTest {

    @Mock
    private ActiveMQConnectionFactory connectionFactory;

    @InjectMocks
    private JmsSessionFactory jmsSessionFactory;

    @Test
    public void shouldCreateAndStartJmsSession() throws Exception {

        final String queueUri = "queueUri";

        final Connection connection = mock(Connection.class);
        final Session session = mock(Session.class);

        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.createSession(false, AUTO_ACKNOWLEDGE)).thenReturn(session);

        assertThat(jmsSessionFactory.session(queueUri), is(session));

        final InOrder inOrder = inOrder(connectionFactory, connection);

        inOrder.verify(connectionFactory).setBrokerURL(queueUri);
        inOrder.verify(connectionFactory).createConnection();
        inOrder.verify(connection).start();
        inOrder.verify(connection).createSession(false, AUTO_ACKNOWLEDGE);

        jmsSessionFactory.close();

        verify(connectionFactory).close();
        verify(connection).close();
        verify(session).close();
    }

    @Test
    public void shouldFailIfJmsSessionDoesNotStart() throws Exception {

        final String queueUri = "some-uri";
        final JMSException jmsException = new JMSException("Ooops");

        when(connectionFactory.createConnection()).thenThrow(jmsException);

        final MessageConsumerException messageConsumerException = assertThrows(
                MessageConsumerException.class,
                () -> jmsSessionFactory.session(queueUri));

        assertThat(messageConsumerException.getMessage(), is("Failed to create JMS session for queue uri 'some-uri'"));
        assertThat(messageConsumerException.getCause(), is(jmsException));

        jmsSessionFactory.close();

        verify(connectionFactory).close();
    }
}