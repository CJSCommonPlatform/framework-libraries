package uk.gov.justice.services.test.utils.core.messaging;

import org.apache.activemq.artemis.jms.client.ActiveMQTopic;

public class TopicFactory {

    public ActiveMQTopic createTopic(final String address) {
       return new ActiveMQTopic(address);
    }
}
