package uk.gov.justice.services.messaging.jms;

import uk.gov.justice.services.messaging.JsonEnvelope;

import jakarta.jms.TextMessage;

public interface EnvelopeConverter extends MessageConverter<JsonEnvelope, TextMessage> {
}