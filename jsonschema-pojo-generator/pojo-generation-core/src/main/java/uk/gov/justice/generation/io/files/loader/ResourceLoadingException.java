package uk.gov.justice.generation.io.files.loader;

/*
 * Exception is thrown if a resource fails to load.
 */
public class ResourceLoadingException extends RuntimeException {

    public ResourceLoadingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
