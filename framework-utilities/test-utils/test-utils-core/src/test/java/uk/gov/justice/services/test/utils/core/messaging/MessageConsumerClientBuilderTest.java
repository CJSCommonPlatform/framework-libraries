package uk.gov.justice.services.test.utils.core.messaging;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.gov.justice.services.test.utils.core.messaging.MessageConsumerClientBuilder.aMessageConsumerClient;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageConsumerClientBuilderTest {

    @Mock
    private ActiveMQConnectionFactory activeMQConnectionFactory;

    @InjectMocks
    private MessageConsumerClientBuilder messageConsumerClientBuilder;

    @Test
    public void shouldHaveStaticFactoryMethod() throws Exception {

        assertThat(aMessageConsumerClient(), is(notNullValue()));
    }

    @Test
    public void shouldCreateMessageConsumerClientWithNoExtraActiveMQConnectionFactoryProperties() throws Exception {

        final MessageConsumerClient messageConsumerClient = messageConsumerClientBuilder.build();
        assertThat(messageConsumerClient, is(notNullValue()));

        verifyNoInteractions(activeMQConnectionFactory);

        final MessageConsumerFactory messageConsumerFactory = getValueOfField(messageConsumerClient, "messageConsumerFactory", MessageConsumerFactory.class);

        assertThat(messageConsumerFactory, is(notNullValue()));

        final JmsSessionFactory jmsSessionFactory = getValueOfField(messageConsumerFactory, "jmsSessionFactory", JmsSessionFactory.class);
        assertThat(jmsSessionFactory, is(notNullValue()));

        final ActiveMQConnectionFactory theActiveMQConnectionFactory = getValueOfField(jmsSessionFactory, "activeMQConnectionFactory", ActiveMQConnectionFactory.class);

        assertThat(theActiveMQConnectionFactory, is(sameInstance(activeMQConnectionFactory)));
    }

    @Test
    public void shouldSetRetryIntervalOnActiveMQConnectionFactory() throws Exception {

        final int retryInterval = 23;

        final MessageConsumerClient messageConsumerClient = messageConsumerClientBuilder
                .withRetryInterval(retryInterval)
                .build();
        assertThat(messageConsumerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setRetryInterval(retryInterval);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }

    @Test
    public void shouldSetMaxRetryIntervalOnActiveMQConnectionFactory() throws Exception {

        final int maxRetryInterval = 23;

        final MessageConsumerClient messageConsumerClient = messageConsumerClientBuilder
                .withMaxRetryInterval(maxRetryInterval)
                .build();
        assertThat(messageConsumerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setMaxRetryInterval(maxRetryInterval);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }

    @Test
    public void shouldSetMaxRetryIntervalMultiplierOnActiveMQConnectionFactory() throws Exception {

        final double retryIntervalMultiplier = 23.1;

        final MessageConsumerClient messageConsumerClient = messageConsumerClientBuilder
                .withRetryIntervalMultiplier(retryIntervalMultiplier)
                .build();
        assertThat(messageConsumerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setRetryIntervalMultiplier(retryIntervalMultiplier);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }

    @Test
    public void shouldSetReconnectAttemptsOnActiveMQConnectionFactory() throws Exception {

        final int reconnectAttempts = 23;

        final MessageConsumerClient messageConsumerClient = messageConsumerClientBuilder
                .withReconnectAttempts(reconnectAttempts)
                .build();
        assertThat(messageConsumerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setReconnectAttempts(reconnectAttempts);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }
}