package uk.gov.justice.services.core.error;

import uk.gov.justice.services.messaging.JsonEnvelope;

public interface JsonEnvelopeProcessingFailureHandler {

    void onJsonEnvelopeProcessingFailure(final JsonEnvelope jsonEnvelope, final Throwable exception);
}
