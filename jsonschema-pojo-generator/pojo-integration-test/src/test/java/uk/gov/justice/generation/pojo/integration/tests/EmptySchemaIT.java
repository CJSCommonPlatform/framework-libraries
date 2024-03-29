package uk.gov.justice.generation.pojo.integration.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmptySchemaIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @BeforeEach
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/tests/empty-schemas");
    }

    @Test
    public void shouldGenerateEmptyClassWithAdditionalPropertiesIfNoAdditionalPropertiesSpecified() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/tests/empty-schema.json");

        final String packageName = "uk.gov.justice.pojo.empty.schemas";

        final List<Class<?>> classes = generatorUtil
                .withClassModifyingPlugin(new AddAdditionalPropertiesToClassPlugin())
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        assertThat(emptySchemaClass.getDeclaredField("additionalProperties"), is(notNullValue()));
        assertThat(emptySchemaClass.getDeclaredField("additionalProperties").getType().getName(), is("java.util.HashMap"));

        assertThat(emptySchemaClass.getDeclaredMethod("getAdditionalProperties"), is(notNullValue()));
        assertThat(emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class), is(notNullValue()));
    }

    @Test
    public void shouldGenerateEmptyClassWithAdditionalPropertiesIfAdditionalPropertiesIsSetToTrue() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/tests/empty-schema-with-additional-properties-true.json");

        final String packageName = "uk.gov.justice.pojo.empty.schemas";

        final List<Class<?>> classes = generatorUtil
                .withClassModifyingPlugin(new AddAdditionalPropertiesToClassPlugin())
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        emptySchemaClass.getDeclaredField("additionalProperties");
        emptySchemaClass.getDeclaredField("additionalProperties").getType().getName();
        emptySchemaClass.getDeclaredMethod("getAdditionalProperties");
        emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
    }

    @Test
    public void shouldGenerateEmptyClassWithAdditionalPropertiesIfAdditionalPropertiesIsSetToFalse() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/tests/empty-schema-with-additional-properties-false.json");
        final String packageName = "uk.gov.justice.pojo.empty.schemas";

        final List<Class<?>> classes = generatorUtil
                .withClassModifyingPlugin(new AddAdditionalPropertiesToClassPlugin())
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        try {
            emptySchemaClass.getDeclaredField("additionalProperties");
            fail();
        } catch (final NoSuchFieldException ignored) {
        }
        try {
            emptySchemaClass.getDeclaredField("additionalProperties").getType().getName();
            fail();
        } catch (final NoSuchFieldException ignored) {
        }
        try {
            emptySchemaClass.getDeclaredMethod("getAdditionalProperties");
            fail();
        } catch (final NoSuchMethodException ignored) {
        }
        try {
            emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
            fail();
        } catch (final NoSuchMethodException ignored) {
        }
    }

}
