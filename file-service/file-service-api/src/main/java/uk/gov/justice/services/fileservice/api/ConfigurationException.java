package uk.gov.justice.services.fileservice.api;

public class ConfigurationException extends FileServiceException {

    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
