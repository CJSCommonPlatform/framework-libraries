package uk.gov.justice.services.messaging.spi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

public class JsonEnvelopeProviderNotFoundExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() throws Exception {
        final JsonEnvelopeProviderNotFoundException exception = new JsonEnvelopeProviderNotFoundException("Test message");
        assertThat(exception.getMessage(), is("Test message"));
    }

}
