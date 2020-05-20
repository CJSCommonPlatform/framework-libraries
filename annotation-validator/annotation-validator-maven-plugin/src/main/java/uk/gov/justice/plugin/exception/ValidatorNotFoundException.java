package uk.gov.justice.plugin.exception;

public class ValidatorNotFoundException extends RuntimeException {

    public ValidatorNotFoundException(final String message) {
        super(message);
    }
}
