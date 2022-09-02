package uk.gov.justice.services.test.utils.core.messaging;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TopicFactoryTest {


    @InjectMocks
    private TopicFactory topicFactory;

    @Test
    public void shouldCreateActiveMQTopic() throws Exception {

        final String topicName = "some-name";
        final ActiveMQTopic topic = topicFactory.createTopic(topicName);

        assertThat(topic.getTopicName(), is(topicName));
    }
}