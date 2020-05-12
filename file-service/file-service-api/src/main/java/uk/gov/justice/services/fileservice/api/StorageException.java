package uk.gov.justice.services.fileservice.api;

public class StorageException extends FileServiceException {

    public StorageException(final String message) {
        super(message);
    }

    public StorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
