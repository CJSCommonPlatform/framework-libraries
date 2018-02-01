package uk.gov.justice.services.core.json;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonSchemaValidatonExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() throws Exception {
        final JsonSchemaValidatonException exception = new JsonSchemaValidatonException("Test message", new Throwable());
        assertThat(exception.getMessage(), is("Test message"));
    }

}
