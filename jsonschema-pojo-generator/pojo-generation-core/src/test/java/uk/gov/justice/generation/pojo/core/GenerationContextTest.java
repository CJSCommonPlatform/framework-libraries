package uk.gov.justice.generation.pojo.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;


@ExtendWith(MockitoExtension.class)
public class GenerationContextTest {

    @Mock
    private Path outputDirectoryPath;

    @Test
    public void shouldReturnOutputDirectoryPath() throws Exception {
        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename, emptyList());

        assertThat(generationContext.getOutputDirectoryPath(), is(outputDirectoryPath));
    }

    @Test
    public void shouldReturnPackageName() throws Exception {
        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename, emptyList());

        assertThat(generationContext.getPackageName(), is(packageName));
    }

    @Test
    public void shouldReturnFileSource() throws Exception {
        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename, emptyList());

        assertThat(generationContext.getSourceFilename(), is(sourceFilename));
    }

    @Test
    public void shouldGetTheCorrectLoggerForTheClass() throws Exception {

        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename, emptyList());

        final Logger logger = generationContext.getLoggerFor(getClass());

        assertThat(logger.getName(), is(getClass().getName()));
    }

    @Test
    public void shouldGetTheCorrectHardCodedClassNames() throws Exception {

        final String packageName = "package.name";
        final String sourceFilename = "filename.json";
        final List<String> hardCodedClassNames = asList("ClassName_1", "ClassName_2", "ClassName_3");
        final GenerationContext generationContext = new GenerationContext(outputDirectoryPath, packageName, sourceFilename, hardCodedClassNames);

        assertThat(generationContext.getIgnoredClassNames(), is(hardCodedClassNames));
    }
}
