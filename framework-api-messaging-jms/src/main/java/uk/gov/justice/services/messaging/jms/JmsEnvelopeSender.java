package uk.gov.justice.services.messaging.jms;

import uk.gov.justice.services.messaging.JsonEnvelope;

/**
 * An envelope producer that sends or publishes an envelope to a queue or topic respectively
 * depending on the destination type.
 */
public interface JmsEnvelopeSender {

    /**
     * Sends envelope to the destination via JMS.
     *
     * @param envelope        envelope to be sent.
     * @param destinationName JNDI name of the JMS destination.
     */
    void send(JsonEnvelope envelope, String destinationName);
}
