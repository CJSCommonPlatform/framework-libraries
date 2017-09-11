package uk.gov.justice.generation.pojo.integration.test;

import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EmptySchemaIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/empty-schemas");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
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

        assertThat(emptySchemaClass.getAnnotation(Event.class), is(notNullValue()));
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

        assertThat(emptySchemaClass.getAnnotation(Event.class), is(notNullValue()));
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

        assertThat(emptySchemaClass.getAnnotation(Event.class), is(notNullValue()));
    }

    private List<Class<?>> setupAndGenerate(final String fileName) {
        final File jsonSchemaFile = new File("src/test/resources/schemas/" + fileName);
        final String packageName = "uk.gov.justice.pojo.empty.schemas";

        return generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath());
    }
}
