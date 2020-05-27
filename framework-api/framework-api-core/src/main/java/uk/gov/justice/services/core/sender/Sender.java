package uk.gov.justice.services.core.sender;

import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;

/**
 * Sends an action to the next layer.
 */
public interface Sender {

    /**
     * Sends envelope to the next component.  The correct sender is injected by the framework.
     *
     * @param envelope with payload T that needs to be sent.
     */
    void send(final Envelope<?> envelope);

    /**
     * Sends envelope to the next component setting system user id.
     *
     * @deprecated use Sender.sendAsAdmin(Envelope<?> envelope) instead.
     *
     * @param envelope JsonEnvelope that needs to be sent.
     */
    @Deprecated
    void sendAsAdmin(final JsonEnvelope envelope);

    /**
     * Sends envelope to the next component setting system user id.
     *
     * @param envelope with payload T that needs to be sent.
     */
    void sendAsAdmin(final Envelope<?> envelope);
}
