package uk.gov.justice.generation.pojo.visitor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class FailedToParseSchemaExceptionTest {

    @Test
    public void shouldConstructFailedToParseSchemaExceptionWithMessage() throws Exception {
        final String message = "Test message";
        final Exception cause = mock(Exception.class);

        final FailedToParseSchemaException schemaException = new FailedToParseSchemaException(message, cause);

        assertThat(schemaException.getMessage(), is(message));
        assertThat(schemaException.getCause(), is(cause));
    }
}
