package uk.gov.justice.generation;

import static java.nio.file.Paths.get;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SchemaPojoGeneratorTest {

    private File sourceOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("target/test-generation");
        sourceOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldConvertSchemaFileToJavaPojo() throws Exception {
        final File schemaFile = get("src/test/resources/schemas/person-schema.json").toFile();
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        when(generatorConfig.getOutputDirectory()).thenReturn(sourceOutputDirectory.toPath());
        when(generatorConfig.getBasePackageName()).thenReturn("uk.gov.justice.generation.pojo");

        final SchemaPojoGenerator schemaPojoGenerator = new SchemaPojoGenerator();

        schemaPojoGenerator.run(schemaFile, generatorConfig);

        final File directory = Paths.get("target/test-generation/uk/gov/justice/generation/pojo").toFile();
        assertThat(directory.exists(), is(true));

        final File[] files = directory.listFiles();
        assertThat(files, notNullValue());
        assertThat(files.length, is(1));
        assertThat(files[0].toPath().toString(), is("target/test-generation/uk/gov/justice/generation/pojo/PersonSchema.java"));
    }

    @Test
    public void shouldUsePluginProviderSetInGeneratorConfigAndNotUseEventAnnotationPlugin() throws Exception {
        final File schemaFile = get("src/test/resources/schemas/example.events.person-event.json").toFile();
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);
        final Map<String, String> properties = new HashMap<>();
        properties.put("pojo-plugin-provider", "uk.gov.justice.generation.pojo.TestPluginProvider");

        when(generatorConfig.getOutputDirectory()).thenReturn(sourceOutputDirectory.toPath());
        when(generatorConfig.getBasePackageName()).thenReturn("uk.gov.justice.generation.event");
        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final SchemaPojoGenerator schemaPojoGenerator = new SchemaPojoGenerator();

        schemaPojoGenerator.run(schemaFile, generatorConfig);

        final File directory = Paths.get("target/test-generation/uk/gov/justice/generation/event").toFile();
        assertThat(directory.exists(), is(true));

        final File[] files = directory.listFiles();
        assertThat(files, notNullValue());
        assertThat(files.length, is(1));
        assertThat(files[0].toPath().toString(), is("target/test-generation/uk/gov/justice/generation/event/PersonEvent.java"));
    }
}
