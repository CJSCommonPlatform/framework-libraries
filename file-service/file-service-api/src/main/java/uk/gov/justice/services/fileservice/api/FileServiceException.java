package uk.gov.justice.services.fileservice.api;

public class FileServiceException extends Exception {

    public FileServiceException(final String message) {
        super(message);
    }

    public FileServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
