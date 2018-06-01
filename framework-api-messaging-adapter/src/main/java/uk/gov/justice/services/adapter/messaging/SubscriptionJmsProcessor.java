package uk.gov.justice.services.adapter.messaging;

import uk.gov.justice.services.core.interceptor.InterceptorChainProcessor;
import uk.gov.justice.services.subscription.SubscriptionManager;

import javax.jms.Message;

public interface SubscriptionJmsProcessor {

    /**
     * Process an incoming JMS message by validating the message and then passing the envelope
     * converted from the message to the given {@link SubscriptionManager}.
     *
     * @param message             a message to be processed
     * @param subscriptionManager a subscription manager
     * @param interceptorChainProcessor a interceptor chain processor
     */
    void process(final Message message,
                 final SubscriptionManager subscriptionManager,
                 final InterceptorChainProcessor interceptorChainProcessor);
}