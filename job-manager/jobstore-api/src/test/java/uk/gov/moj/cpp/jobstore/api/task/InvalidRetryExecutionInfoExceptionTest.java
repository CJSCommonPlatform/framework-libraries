package uk.gov.moj.cpp.jobstore.api.task;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class InvalidRetryExecutionInfoExceptionTest {

    @Test
    public void shouldCreateInstanceOfInvalidRetryExecutionInfoExceptionWithMessage() throws Exception {
        final InvalidRetryExecutionInfoException exception = new InvalidRetryExecutionInfoException("Test message");
        assertThat(exception.getMessage(), is("Test message"));
        assertThat(exception, instanceOf(RuntimeException.class));
    }

}