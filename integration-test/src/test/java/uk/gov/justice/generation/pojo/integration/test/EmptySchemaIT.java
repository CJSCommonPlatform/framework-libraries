package uk.gov.justice.generation.pojo.integration.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EmptySchemaIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/empty-schemas");
    }

    @Test
    public void shouldGenerateAnEmptyClassWithAdditionalPropertiesIfNoAdditionalPropertiesSpecified() throws Exception {

        final List<Class<?>> classes = setupAndGenerate("empty-schema.json");

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        assertThat(emptySchemaClass.getDeclaredField("additionalProperties"), is(notNullValue()));
        assertThat(emptySchemaClass.getDeclaredField("additionalProperties").getType().getName(), is("java.util.Map"));

        assertThat(emptySchemaClass.getDeclaredMethod("getAdditionalProperties"), is(notNullValue()));
        assertThat(emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class), is(notNullValue()));
    }

    @Test
    public void shouldGenerateAnEmptyClassWithAdditionalPropertiesIfoAdditionalPropertiesIsSetToTrue() throws Exception {

        final List<Class<?>> classes = setupAndGenerate("empty-schema-with-additional-properties-true.json");

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        emptySchemaClass.getDeclaredField("additionalProperties");
        emptySchemaClass.getDeclaredField("additionalProperties").getType().getName();
        emptySchemaClass.getDeclaredMethod("getAdditionalProperties");
        emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
    }

    @Test
    public void shouldGenerateAnEmptyClassWithAdditionalPropertiesIfoAdditionalPropertiesIsSetToFalse() throws Exception {

        final List<Class<?>> classes = setupAndGenerate("empty-schema-with-additional-properties-false.json");

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

    private List<Class<?>> setupAndGenerate(final String fileName) {
        final File jsonSchemaFile = new File("src/test/resources/schemas/" + fileName);
        final String packageName = "uk.gov.justice.pojo.empty.schemas";

        return generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);
    }
}
