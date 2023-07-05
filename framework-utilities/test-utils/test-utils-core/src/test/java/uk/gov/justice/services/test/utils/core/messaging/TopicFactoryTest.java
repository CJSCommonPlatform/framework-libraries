package uk.gov.justice.services.test.utils.core.messaging;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
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