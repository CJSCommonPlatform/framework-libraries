package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class UnsupportedSchemaExceptionTest {

    @Test
    public void shouldConstructUnsupportedSchemaExceptionWithMessage() throws Exception {
        final UnsupportedSchemaException schemaException = new UnsupportedSchemaException("Test message");

        assertThat(schemaException.getMessage(), is("Test message"));
    }
}