package uk.gov.justice.services.core.json;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonSchemaValidationExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() throws Exception {
        final JsonSchemaValidationException exception = new JsonSchemaValidationException("Test message");
        assertThat(exception.getMessage(), is("Test message"));
    }

    @Test
    public void shouldCreateExceptionWithMessageAndCause() throws Exception {
        final JsonSchemaValidationException exception = new JsonSchemaValidationException("Test message", new Throwable());
        assertThat(exception.getMessage(), is("Test message"));
    }
}
