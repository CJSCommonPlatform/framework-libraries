package uk.gov.justice.services.core.enveloper.spi;

public class EnveloperProviderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -9007593512368498735L;

    public EnveloperProviderNotFoundException(final String message) {
        super(message);
    }
}