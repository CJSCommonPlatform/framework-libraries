package uk.gov.justice.services.adapter.messaging;

import javax.jms.Message;

public interface SubscriptionJmsProcessor {

    /**
     * Process an incoming JMS message by validating the message and then passing the envelope
     * converted from the message to the given {@link SubscriptionManager}.
     *
     * @param subscriptionManager a subscription manager
     * @param message             a message to be processed
     */
    void process(final SubscriptionManager subscriptionManager, final Message message);
}