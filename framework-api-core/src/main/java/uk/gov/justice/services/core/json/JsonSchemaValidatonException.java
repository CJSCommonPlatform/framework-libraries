package uk.gov.justice.services.core.json;

public class JsonSchemaValidatonException extends RuntimeException {

    private static final long serialVersionUID =  -9004984738197385375L;

    public JsonSchemaValidatonException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
