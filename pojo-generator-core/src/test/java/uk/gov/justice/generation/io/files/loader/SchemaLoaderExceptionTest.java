package uk.gov.justice.generation.io.files.loader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class SchemaLoaderExceptionTest {

    @Test
    public void shouldCreateSchemaLoaderExceptionWithMessage() throws Exception {
        final SchemaLoaderException schemaLoaderException = new SchemaLoaderException("test message");
        assertThat(schemaLoaderException.getMessage(), is("test message"));
    }

    @Test
    public void shouldCreateSchemaLoaderExceptionWithMessageAndCause() throws Exception {
        final Exception cause = mock(Exception.class);
        final SchemaLoaderException schemaLoaderException = new SchemaLoaderException("test message", cause);

        assertThat(schemaLoaderException.getMessage(), is("test message"));
        assertThat(schemaLoaderException.getCause(), is(cause));
    }
}