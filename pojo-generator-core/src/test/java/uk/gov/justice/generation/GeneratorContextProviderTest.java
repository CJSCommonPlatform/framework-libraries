package uk.gov.justice.generation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorContextProviderTest {

    @Mock
    private JavaFileSimpleNameLister javaFileSimpleNameLister;

    @InjectMocks
    private GeneratorContextProvider generatorContextProvider;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideGeneratorContext() throws Exception {
        final String packageName = "package.name";
        final String schemaFilename = "schemaFilename";
        final Path outputDirectory = mock(Path.class);
        final List<String> hardCodedClassNames = mock(List.class);

        final List<Path> sourcePaths = mock(List.class);
        final File schemaFile = mock(File.class);
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        when(generatorConfig.getSourcePaths()).thenReturn(sourcePaths);
        when(generatorConfig.getOutputDirectory()).thenReturn(outputDirectory);
        when(generatorConfig.getBasePackageName()).thenReturn(packageName);
        when(javaFileSimpleNameLister.findSimpleNames(sourcePaths, outputDirectory, packageName)).thenReturn(hardCodedClassNames);

        when(schemaFile.getName()).thenReturn(schemaFilename);

        final GenerationContext generationContext = generatorContextProvider.create(schemaFile, generatorConfig);

        assertThat(generationContext.getPackageName(), is(packageName));
        assertThat(generationContext.getOutputDirectoryPath(), is(outputDirectory));
        assertThat(generationContext.getSourceFilename(), is(schemaFilename));
        assertThat(generationContext.getIgnoredClassNames(), is(hardCodedClassNames));
    }
}
