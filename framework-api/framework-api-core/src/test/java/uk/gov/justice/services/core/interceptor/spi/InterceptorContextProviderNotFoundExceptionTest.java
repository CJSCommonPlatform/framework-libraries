package uk.gov.justice.services.core.interceptor.spi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

public class InterceptorContextProviderNotFoundExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() throws Exception {
        final InterceptorContextProviderNotFoundException exception = new InterceptorContextProviderNotFoundException("Test message");
        assertThat(exception.getMessage(), is("Test message"));
    }

}