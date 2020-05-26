package uk.gov.justice.generation.pojo.core;

/**
 * Exception thrown if the specified JSON schema used for the generation of our java POJOs
 * contains unsupported json schema elements
 */
public class UnsupportedSchemaException extends RuntimeException {

    public UnsupportedSchemaException(final String message) {
        super(message);
    }
}
