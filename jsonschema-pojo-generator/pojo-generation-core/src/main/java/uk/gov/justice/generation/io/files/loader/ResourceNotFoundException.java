package uk.gov.justice.generation.io.files.loader;

/**
 * Exception is thrown if a resource is not found
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
