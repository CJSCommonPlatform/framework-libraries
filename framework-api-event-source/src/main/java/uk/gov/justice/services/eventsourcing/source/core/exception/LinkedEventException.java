package uk.gov.justice.services.eventsourcing.source.core.exception;

public class LinkedEventException extends RuntimeException {

    public LinkedEventException(final String message) {
        super(message);
    }

    public LinkedEventException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
