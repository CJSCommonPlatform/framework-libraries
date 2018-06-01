package uk.gov.justice.services.subscription;

import uk.gov.justice.services.core.interceptor.InterceptorChainProcessor;
import uk.gov.justice.services.messaging.JsonEnvelope;

/**
 * The SubscriptionManager processes a {@link JsonEnvelope}
 */
public interface SubscriptionManager {

    /**
     * Process the {@link JsonEnvelope}
     *
     * @param jsonEnvelope the {@link JsonEnvelope} to be processed
     * @param interceptorChainProcessor the {@link InterceptorChainProcessor} to start processing JsonEnvelope
     */
    void process(final JsonEnvelope jsonEnvelope, final InterceptorChainProcessor interceptorChainProcessor);

    /**
     * Starts the subscription process
     */
    void startSubscription();
}
