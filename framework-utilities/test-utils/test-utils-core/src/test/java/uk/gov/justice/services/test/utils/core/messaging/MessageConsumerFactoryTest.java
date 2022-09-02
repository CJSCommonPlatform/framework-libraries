package uk.gov.justice.services.test.utils.core.messaging;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import java.util.Set;

import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageConsumerFactoryTest {

    @Mock
    private JmsSessionFactory jmsSessionFactory;

    @Mock
    private TopicFactory topicFactory;

    @InjectMocks
    private MessageConsumerFactory messageConsumerFactory;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateAndStartTheMessageConsumer() throws Exception {

        final String messageSelector = "message-selector";
        final String queueUri = "queue-uri";
        final String topicName = "topic-name";

        final Session session = mock(Session.class);
        final ActiveMQTopic topic = mock(ActiveMQTopic.class);
        final MessageConsumer messageConsumer = mock(MessageConsumer.class);

        when(jmsSessionFactory.session(queueUri)).thenReturn(session);
        when(topicFactory.createTopic(topicName)).thenReturn(topic);
        when(session.createConsumer(topic, messageSelector)).thenReturn(messageConsumer);

        assertThat(messageConsumerFactory.createAndStart(messageSelector, queueUri, topicName), is(messageConsumer));

        assertThat(getValueOfField(messageConsumerFactory, "session", Session.class), is(session));
        final Set<MessageConsumer> messageConsumers = getValueOfField(messageConsumerFactory, "messageConsumers", Set.class);

        assertThat(messageConsumers.size(), is(1));
        assertThat(messageConsumers, hasItem(messageConsumer));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldNotRecreateTheSessionIfRestarted() throws Exception {

        final String messageSelector = "message-selector";
        final String queueUri = "queue-uri";
        final String topicName_1 = "topic-name-1";
        final String topicName_2 = "topic-name-2";

        final Session session = mock(Session.class);
        final ActiveMQTopic topic_1 = mock(ActiveMQTopic.class, "topic_1");
        final MessageConsumer messageConsumer_1 = mock(MessageConsumer.class, "messageConsumer_1");

        final ActiveMQTopic topic_2 = mock(ActiveMQTopic.class, "topic_2");
        final MessageConsumer messageConsumer_2 = mock(MessageConsumer.class, "messageConsumer_2");

        when(jmsSessionFactory.session(queueUri)).thenReturn(session);
        when(topicFactory.createTopic(topicName_1)).thenReturn(topic_1);
        when(session.createConsumer(topic_1, messageSelector)).thenReturn(messageConsumer_1);

        when(topicFactory.createTopic(topicName_2)).thenReturn(topic_2);
        when(session.createConsumer(topic_2, messageSelector)).thenReturn(messageConsumer_2);

        assertThat(messageConsumerFactory.createAndStart(messageSelector, queueUri, topicName_1), is(messageConsumer_1));
        assertThat(messageConsumerFactory.createAndStart(messageSelector, queueUri, topicName_2), is(messageConsumer_2));

        assertThat(getValueOfField(messageConsumerFactory, "session", Session.class), is(session));
        final Set<MessageConsumer> messageConsumers = getValueOfField(messageConsumerFactory, "messageConsumers", Set.class);

        assertThat(messageConsumers.size(), is(2));
        assertThat(messageConsumers, hasItem(messageConsumer_1));
        assertThat(messageConsumers, hasItem(messageConsumer_2));

        verify(jmsSessionFactory, times(1)).session(queueUri);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCloseEverythingCorrectly() throws Exception {

        final String messageSelector = "message-selector";
        final String queueUri = "queue-uri";
        final String topicName_1 = "topic-name-1";
        final String topicName_2 = "topic-name-2";

        final Session session = mock(Session.class);
        final ActiveMQTopic topic_1 = mock(ActiveMQTopic.class, "topic_1");
        final MessageConsumer messageConsumer_1 = mock(MessageConsumer.class, "messageConsumer_1");

        final ActiveMQTopic topic_2 = mock(ActiveMQTopic.class, "topic_2");
        final MessageConsumer messageConsumer_2 = mock(MessageConsumer.class, "messageConsumer_2");

        when(jmsSessionFactory.session(queueUri)).thenReturn(session);
        when(topicFactory.createTopic(topicName_1)).thenReturn(topic_1);
        when(session.createConsumer(topic_1, messageSelector)).thenReturn(messageConsumer_1);

        when(topicFactory.createTopic(topicName_2)).thenReturn(topic_2);
        when(session.createConsumer(topic_2, messageSelector)).thenReturn(messageConsumer_2);

        assertThat(messageConsumerFactory.createAndStart(messageSelector, queueUri, topicName_1), is(messageConsumer_1));
        assertThat(messageConsumerFactory.createAndStart(messageSelector, queueUri, topicName_2), is(messageConsumer_2));

        assertThat(getValueOfField(messageConsumerFactory, "session", Session.class), is(session));
        final Set<MessageConsumer> messageConsumers = getValueOfField(messageConsumerFactory, "messageConsumers", Set.class);

        assertThat(messageConsumers.size(), is(2));
        assertThat(messageConsumers, hasItem(messageConsumer_1));
        assertThat(messageConsumers, hasItem(messageConsumer_2));

        messageConsumerFactory.close();

        verify(messageConsumer_1).close();
        verify(messageConsumer_2).close();
        verify(session).close();
        verify(jmsSessionFactory).close();
        assertThat(messageConsumers.isEmpty(), is(true));
        assertThat(getValueOfField(messageConsumerFactory, "session", Session.class), is(nullValue()));
        assertThat(getValueOfField(messageConsumerFactory, "jmsSessionFactory", JmsSessionFactory.class), is(jmsSessionFactory));
    }
}