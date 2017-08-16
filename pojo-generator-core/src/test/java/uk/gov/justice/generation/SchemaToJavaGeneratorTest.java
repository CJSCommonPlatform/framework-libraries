package uk.gov.justice.generation;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;


public class SchemaToJavaGeneratorTest {

    @Test
    public void shouldConvertSchemaFileToJavaPojo() throws Exception {
        final File schemaFile = get("src/test/resources/schemas/person-schema.json").toFile();
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);

        when(generatorConfig.getOutputDirectory()).thenReturn(Paths.get("target/test-generation"));
        when(generatorConfig.getBasePackageName()).thenReturn("uk.gov.justice.generation");

        final SchemaToJavaGenerator schemaToJavaGenerator = new SchemaToJavaGenerator();

        schemaToJavaGenerator.run(schemaFile, generatorConfig);

        final File directory = Paths.get("target/test-generation/uk/gov/justice/generation").toFile();
        assertThat(directory.exists(), is(true));

        final File[] files = directory.listFiles();
        assertThat(files, notNullValue());
        assertThat(files.length, is(1));
        assertThat(files[0].toPath().toString(), is("target/test-generation/uk/gov/justice/generation/PersonSchema.java"));
    }
}