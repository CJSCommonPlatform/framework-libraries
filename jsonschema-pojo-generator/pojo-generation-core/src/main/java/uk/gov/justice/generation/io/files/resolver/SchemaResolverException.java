package uk.gov.justice.generation.io.files.resolver;

/**
 * Exception thrown if resolving a json schema resource fails
 */
public class SchemaResolverException extends RuntimeException {

    public SchemaResolverException(final String message) {
        super(message);
    }

    public SchemaResolverException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
