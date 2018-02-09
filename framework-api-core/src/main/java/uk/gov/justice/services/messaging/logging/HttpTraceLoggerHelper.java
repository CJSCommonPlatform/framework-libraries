package uk.gov.justice.services.messaging.logging;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Handles message logging for ReaderInterceptorContext headers.
 */
public interface HttpTraceLoggerHelper {

    /**
     * Converts {@link HttpHeaders} headers to a readable log trace message
     * @param headers
     * @return
     */
    String toHttpHeaderTrace(final HttpHeaders headers);

    /**
     * Converts {@link MultivaluedMap} headers to a readable log trace message
     * @param headers
     * @return
     */
    String toHttpHeaderTrace(final MultivaluedMap<String, String> headers);

}