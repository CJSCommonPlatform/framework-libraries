package uk.gov.justice.services.messaging.spi;

public class EnvelopeBuilderProviderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -9007543497548965235L;

    public EnvelopeBuilderProviderNotFoundException(final String message) {
        super(message);
    }
}