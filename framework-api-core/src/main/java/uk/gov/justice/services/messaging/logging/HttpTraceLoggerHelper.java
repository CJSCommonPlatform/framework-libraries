package uk.gov.justice.services.messaging.logging;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

public interface HttpTraceLoggerHelper {

    String toHttpHeaderTrace(final HttpHeaders headers);
    String toHttpHeaderTrace(final MultivaluedMap<String, String> headers);
}