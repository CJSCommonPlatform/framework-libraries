package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;


@RunWith(MockitoJUnitRunner.class)
public class GenerationContextTest {

    @Mock
    private Path outputDirectoryPath;

    @Test
    public void shouldReturnOutputDirectoryPath() throws Exception {
        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename);

        assertThat(generationContext.getOutputDirectoryPath(), is(outputDirectoryPath));
    }

    @Test
    public void shouldReturnPackageName() throws Exception {
        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename);

        assertThat(generationContext.getPackageName(), is(packageName));
    }

    @Test
    public void shouldReturnFileSource() throws Exception {
        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename);

        assertThat(generationContext.getSourceFilename(), is(sourceFilename));
    }

    @Test
    public void shouldGetTheCorrectLoggerForTheClass() throws Exception {

        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename);

        final Logger logger = generationContext.getLoggerFor(getClass());

        assertThat(logger.getName(), is(getClass().getName()));
    }
}
