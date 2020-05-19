package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class GenerationExceptionTest {

    @Test
    public void shouldConstructGenerationExceptionWithMessage() throws Exception {
        assertThat(new GenerationException("test message").getMessage(), is("test message"));
    }
}