package uk.gov.justice.generation.pojo.write;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class SourceCodeWriteExceptionTest {

    @Test
    public void shouldConstructExceptionWithMessage() throws Exception {
        final SourceCodeWriteException exception = new SourceCodeWriteException("test message");

        assertThat(exception.getMessage(), is("test message"));
    }

    @Test
    public void shouldConstructExceptionWithMessageAndCause() throws Exception {
        final Exception cause = mock(Exception.class);
        final SourceCodeWriteException exception = new SourceCodeWriteException("test message", cause);

        assertThat(exception.getMessage(), is("test message"));
        assertThat(exception.getCause(), is(cause));
    }
}