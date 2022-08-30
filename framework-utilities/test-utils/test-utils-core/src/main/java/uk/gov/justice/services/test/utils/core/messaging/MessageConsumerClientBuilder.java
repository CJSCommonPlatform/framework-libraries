package uk.gov.justice.services.test.utils.core.messaging;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import com.google.common.annotations.VisibleForTesting;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageConsumerClientBuilder {

    private Optional<Integer> retryInterval = empty();
    private Optional<Integer> maxRetryInterval = empty();
    private Optional<Double> retryIntervalMultiplier = empty();
    private Optional<Integer> reconnectAttempts = empty();

    private final ActiveMQConnectionFactory activeMQConnectionFactory;

    public static MessageConsumerClientBuilder aMessageConsumerClient() {
        return new MessageConsumerClientBuilder(new ActiveMQConnectionFactory());
    }

    @VisibleForTesting
    private MessageConsumerClientBuilder(final ActiveMQConnectionFactory activeMQConnectionFactory) {
        this.activeMQConnectionFactory = activeMQConnectionFactory;
    }

    public MessageConsumerClientBuilder withRetryInterval(final int retryInterval) {
        this.retryInterval = of(retryInterval);
        return this;
    }

    public MessageConsumerClientBuilder withMaxRetryInterval(final int maxRetryInterval) {
        this.maxRetryInterval = of(maxRetryInterval);
        return this;
    }

    public MessageConsumerClientBuilder withRetryIntervalMultiplier(final double retryIntervalMultiplier) {
        this.retryIntervalMultiplier = of(retryIntervalMultiplier);
        return this;
    }

    public MessageConsumerClientBuilder withReconnectAttempts(int reconnectAttempts) {
        this.reconnectAttempts = of(reconnectAttempts);

        return this;
    }

    public MessageConsumerClient build() {

        retryInterval.ifPresent(activeMQConnectionFactory::setRetryInterval);
        maxRetryInterval.ifPresent(activeMQConnectionFactory::setMaxRetryInterval);
        retryIntervalMultiplier.ifPresent(activeMQConnectionFactory::setRetryIntervalMultiplier);
        reconnectAttempts.ifPresent(activeMQConnectionFactory::setReconnectAttempts);

        final JmsSessionFactory jmsSessionFactory = new JmsSessionFactory(activeMQConnectionFactory);

        return new MessageConsumerClient(jmsSessionFactory);
    }
}
