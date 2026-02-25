package uk.gov.justice.services.clients.core;

public class HttpCallerResponse {

    private final int statusCode;
    private final String body;

    public HttpCallerResponse(final int statusCode, final String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
