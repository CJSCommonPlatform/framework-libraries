package uk.gov.justice.services.framework.system.errors;

import uk.gov.justice.services.messaging.JsonEnvelope;

public interface SystemErrorService {

    void reportError(
            final String messageId,
            final String componentName,
            final JsonEnvelope jsonEnvelope,
            final Throwable exception);
}
