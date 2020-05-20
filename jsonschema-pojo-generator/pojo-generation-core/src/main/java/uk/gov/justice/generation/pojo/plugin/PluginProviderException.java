package uk.gov.justice.generation.pojo.plugin;

public class PluginProviderException extends RuntimeException {

    public PluginProviderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PluginProviderException(final String message) {
        super(message);
    }
}
