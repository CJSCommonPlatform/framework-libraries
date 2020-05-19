package uk.gov.justice.services.test.utils.core.reflection;

public class ReflectionException extends RuntimeException {
    public ReflectionException(final String message) {
        super(message);
    }

    public ReflectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
