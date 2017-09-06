package uk.gov.justice.generation.pojo.visitor;

public class FailedToParseSchemaException extends RuntimeException {

    public FailedToParseSchemaException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
