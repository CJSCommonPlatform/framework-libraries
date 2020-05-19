package uk.gov.justice.generation.pojo.write;

public class SourceCodeWriteException extends RuntimeException {

    public SourceCodeWriteException(final String message) {
        super(message);
    }

    public SourceCodeWriteException(final String message, final Throwable cause) {
        super(message, cause);
    }
}