package uk.gov.justice.services.fileservice.api;

public class DataIntegrityException extends FileServiceException {

    public DataIntegrityException(final String message) {
        super(message);
    }

    public DataIntegrityException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
