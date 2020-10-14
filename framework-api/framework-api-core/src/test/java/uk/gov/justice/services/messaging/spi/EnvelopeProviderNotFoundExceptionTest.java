package uk.gov.justice.services.messaging.spi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

public class EnvelopeProviderNotFoundExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() throws Exception {
        final EnvelopeProviderNotFoundException exception = new EnvelopeProviderNotFoundException("Test message");
        assertThat(exception.getMessage(), is("Test message"));
    }
}