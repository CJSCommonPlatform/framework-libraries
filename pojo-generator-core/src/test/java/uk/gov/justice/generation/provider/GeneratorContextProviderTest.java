package uk.gov.justice.generation.provider;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PackageAndClassNameParser;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.nio.file.Path;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorContextProviderTest {

    @Mock
    private JavaFileSimpleNameLister javaFileSimpleNameLister;

    @Mock
    private PackageAndClassNameParser packageAndClassNameParser;

    @InjectMocks
    private GeneratorContextProvider generatorContextProvider;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProvideGeneratorContext() throws Exception {
        final String packageName = "uk.gov.justice.standards.events";
        final String schemaFilename = "schemaFilename";
        final Path outputDirectory = mock(Path.class);
        final List<String> hardCodedClassNames = mock(List.class);
        final String schemaId = "http://justice.gov.uk/standards/events/address.schema.json";

        final List<Path> sourcePaths = mock(List.class);
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);
        final Schema schema = mock(Schema.class);

        when(generatorConfig.getSourcePaths()).thenReturn(sourcePaths);
        when(generatorConfig.getOutputDirectory()).thenReturn(outputDirectory);
        when(schema.getId()).thenReturn(schemaId);
        when(packageAndClassNameParser.packageNameFrom(schemaId)).thenReturn(of(packageName));

        when(javaFileSimpleNameLister.findSimpleNames(sourcePaths, outputDirectory, packageName)).thenReturn(hardCodedClassNames);
        when(generatorConfig.getBasePackageName()).thenReturn(packageName);

        final GenerationContext generationContext = generatorContextProvider.create(schemaFilename, schema, generatorConfig);

        assertThat(generationContext.getPackageName(), is(packageName));
        assertThat(generationContext.getOutputDirectoryPath(), is(outputDirectory));
        assertThat(generationContext.getSourceFilename(), is(schemaFilename));
        assertThat(generationContext.getIgnoredClassNames(), is(hardCodedClassNames));
    }
}
