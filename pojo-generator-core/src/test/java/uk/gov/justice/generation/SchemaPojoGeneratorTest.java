package uk.gov.justice.generation;

import static java.nio.file.Paths.get;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.core.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Paths;

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
        final PojoGeneratorProperties properties = pojoGeneratorPropertiesBuilder().build();

        when(generatorConfig.getOutputDirectory()).thenReturn(sourceOutputDirectory.toPath());
        when(generatorConfig.getBasePackageName()).thenReturn("uk.gov.justice.generation.pojo");
        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final Generator<File> schemaPojoGenerator = new SchemaPojoGeneratorFactory().create();

        schemaPojoGenerator.run(schemaFile, generatorConfig);

        final File directory = Paths.get("target/test-generation/uk/gov/justice/generation/pojo").toFile();
        assertThat(directory.exists(), is(true));

        final File[] files = directory.listFiles();
        assertThat(files, notNullValue());
        assertThat(files.length, is(1));
        assertThat(files[0].toPath().toString(), is("target/test-generation/uk/gov/justice/generation/pojo/PersonSchema.java"));
    }

    @Test
    public void shouldUseRooClassNameSettingInGeneratorProperties() throws Exception {
        final File schemaFile = get("src/test/resources/schemas/person-schema.json").toFile();
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);
        final PojoGeneratorProperties properties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("TestClassName")
                .build();

        when(generatorConfig.getOutputDirectory()).thenReturn(sourceOutputDirectory.toPath());
        when(generatorConfig.getBasePackageName()).thenReturn("uk.gov.justice.generation.pojo");
        when(generatorConfig.getGeneratorProperties()).thenReturn(properties);

        final Generator<File> schemaPojoGenerator = new SchemaPojoGeneratorFactory().create();

        schemaPojoGenerator.run(schemaFile, generatorConfig);

        final File directory = Paths.get("target/test-generation/uk/gov/justice/generation/pojo").toFile();
        assertThat(directory.exists(), is(true));

        final File[] files = directory.listFiles();
        assertThat(files, notNullValue());
        assertThat(files.length, is(1));
        assertThat(files[0].toPath().toString(), is("target/test-generation/uk/gov/justice/generation/pojo/TestClassName.java"));
    }
}
