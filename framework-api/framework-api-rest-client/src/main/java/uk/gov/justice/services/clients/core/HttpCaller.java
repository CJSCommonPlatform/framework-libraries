package uk.gov.justice.services.clients.core;

import java.util.Map;

public interface HttpCaller {

    /**
     * Make a synchronous GET request to the specified URL with the given headers.
     *
     * @param url     the full URL to send the GET request to
     * @param headers a map of header name to header value to include in the request
     * @return the response containing the status code and body
     */
    HttpCallerResponse get(String url, Map<String, Object> headers);
}
