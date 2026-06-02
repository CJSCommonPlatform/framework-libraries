package uk.gov.justice.services.test.utils.persistence;

public class NoEntityManagerFieldFoundException extends RuntimeException {

    public NoEntityManagerFieldFoundException(final String message) {
        super(message);
    }
}
