package uk.gov.justice.services.core.json;

/**
 * Exception thrown when JSON payload is not valid against the relevant JSON schema.
 */
public class JsonSchemaValidationException extends RuntimeException {

    private static final long serialVersionUID =  -9004984738197385375L;

    public JsonSchemaValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonSchemaValidationException(final String message) {
        super(message);
    }

}
