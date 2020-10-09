package uk.gov.justice.services.common.configuration;

public class JndiNameNotFoundException extends RuntimeException {

    public JndiNameNotFoundException(final String message) {
        super(message);
    }
}
