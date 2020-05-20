package uk.gov.justice.services.core.interceptor.spi;

public class InterceptorContextProviderNotFoundException extends RuntimeException {

    private static final long serialVersionUID =  -9004536849208475775L;

    public InterceptorContextProviderNotFoundException(final String message) {
        super(message);
    }
}