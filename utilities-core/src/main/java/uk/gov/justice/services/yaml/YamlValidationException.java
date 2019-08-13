package uk.gov.justice.services.yaml;

public class YamlValidationException extends RuntimeException{

    public YamlValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
