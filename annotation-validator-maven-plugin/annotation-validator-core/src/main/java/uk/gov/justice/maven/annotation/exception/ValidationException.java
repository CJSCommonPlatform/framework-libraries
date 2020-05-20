package uk.gov.justice.maven.annotation.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(final String message) {
        super(message);
    }
}
