package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;


@RunWith(MockitoJUnitRunner.class)
public class GenerationContextTest {

    @Mock
    private Path outputDirectoryPath;

    @InjectMocks
    private GenerationContext generationContext;

    @Test
    public void shouldReturnOutputDirectoryPath() throws Exception {
        assertThat(generationContext.getOutputDirectoryPath(), is(outputDirectoryPath));
    }

    @Test
    public void shouldGetTheCorrectLoggerForTheClass() throws Exception {

        final Logger logger = generationContext.getLoggerFor(getClass());

        assertThat(logger.getName(), is(getClass().getName()));
    }
}
