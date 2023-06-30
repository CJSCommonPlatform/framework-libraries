package uk.gov.justice.generation.provider;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PackageNameParser;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.nio.file.Path;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GeneratorContextProviderTest {

    @Mock
    private JavaFileSimpleNameLister javaFileSimpleNameLister;

    @Mock
    private PackageNameParser packageNameParser;

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
        when(packageNameParser.packageNameFrom(schemaId)).thenReturn(of(packageName));

        when(javaFileSimpleNameLister.findSimpleNames(sourcePaths, outputDirectory, packageName)).thenReturn(hardCodedClassNames);

        final GenerationContext generationContext = generatorContextProvider.create(schemaFilename, schema, generatorConfig);

        assertThat(generationContext.getPackageName(), is(packageName));
        assertThat(generationContext.getOutputDirectoryPath(), is(outputDirectory));
        assertThat(generationContext.getSourceFilename(), is(schemaFilename));
        assertThat(generationContext.getIgnoredClassNames(), is(hardCodedClassNames));
    }
}
