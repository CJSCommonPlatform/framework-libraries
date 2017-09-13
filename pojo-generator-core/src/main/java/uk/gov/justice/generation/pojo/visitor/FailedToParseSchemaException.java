package uk.gov.justice.generation.pojo.visitor;

/**
 * Thrown if any failure occurs while parsing the schema.
 */
public class FailedToParseSchemaException extends RuntimeException {

    public FailedToParseSchemaException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
