package uk.gov.justice.services.adapter.messaging;

import uk.gov.justice.services.messaging.JsonEnvelope;

/**
 * The SubscriptionManager processes a {@link JsonEnvelope}
 */
public interface SubscriptionManager {

    /**
     * Process the {@link JsonEnvelope}
     *
     * @param jsonEnvelope the {@link JsonEnvelope} to be processed
     */
    void process(final JsonEnvelope jsonEnvelope);
}
