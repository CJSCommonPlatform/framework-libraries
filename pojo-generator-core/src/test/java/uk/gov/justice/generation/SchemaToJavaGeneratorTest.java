package uk.gov.justice.generation;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


public class SchemaToJavaGeneratorTest {

    @Test
    public void shouldConvertSchemaFileToJavaPojo() throws Exception {
        final File schemaFile = get("src/test/resources/schemas/person-schema.json").toFile();
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        when(generatorConfig.getOutputDirectory()).thenReturn(Paths.get("target/test-generation"));
        when(generatorConfig.getBasePackageName()).thenReturn("uk.gov.justice.generation.pojo");

        final SchemaToJavaGenerator schemaToJavaGenerator = new SchemaToJavaGenerator();

        schemaToJavaGenerator.run(schemaFile, generatorConfig);

        final File directory = Paths.get("target/test-generation/uk/gov/justice/generation/pojo").toFile();
        assertThat(directory.exists(), is(true));

        final File[] files = directory.listFiles();
        assertThat(files, notNullValue());
        assertThat(files.length, is(1));
        assertThat(files[0].toPath().toString(), is("target/test-generation/uk/gov/justice/generation/pojo/PersonSchema.java"));

        final String javaSource = loadFileAsString(files[0].toPath());

        assertThat(javaSource, not(containsString("@Event(\"example.events.person-event\")")));
    }

    @Test
    public void shouldConvertSchemaFileToJavaPojoWithEventAnnotation() throws Exception {
        final File schemaFile = get("src/test/resources/schemas/example.events.person-event.json").toFile();
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        when(generatorConfig.getOutputDirectory()).thenReturn(Paths.get("target/test-generation"));
        when(generatorConfig.getBasePackageName()).thenReturn("uk.gov.justice.generation.event");

        final SchemaToJavaGenerator schemaToJavaGenerator = new SchemaToJavaGenerator();

        schemaToJavaGenerator.run(schemaFile, generatorConfig);

        final File directory = Paths.get("target/test-generation/uk/gov/justice/generation/event").toFile();
        assertThat(directory.exists(), is(true));

        final File[] files = directory.listFiles();
        assertThat(files, notNullValue());
        assertThat(files.length, is(1));
        assertThat(files[0].toPath().toString(), is("target/test-generation/uk/gov/justice/generation/event/PersonEvent.java"));

        final String javaSource = loadFileAsString(files[0].toPath());

        assertThat(javaSource, containsString("@Event(\"example.events.person-event\")"));
    }

    private String loadFileAsString(final Path path) throws IOException {
        try (final FileReader reader = new FileReader(path.toFile())) {
            return IOUtils.toString(reader);
        }
    }
}