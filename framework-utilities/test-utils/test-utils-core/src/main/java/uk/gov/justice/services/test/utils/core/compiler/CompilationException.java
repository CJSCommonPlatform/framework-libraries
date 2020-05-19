package uk.gov.justice.services.test.utils.core.compiler;

public class CompilationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CompilationException(final String message) {
        super(message);
    }

    public CompilationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}