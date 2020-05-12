package uk.gov.justice.schema.catalog.exception;

public class InvalidJsonFileException extends RuntimeException {

    public InvalidJsonFileException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

}
