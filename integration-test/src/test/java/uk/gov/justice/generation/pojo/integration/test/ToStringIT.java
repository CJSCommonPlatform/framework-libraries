package uk.gov.justice.generation.pojo.integration.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ToStringIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/to/string");
    }

    @Test
    public void shouldAddToStringMethodUsingAllFields() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/student-to-string.json");
        final String packageName = "uk.gov.justice.pojo.string";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Class<?> studentClass = newClasses.get(0);

        final Object student = classInstantiator.newInstance(studentClass, 23, "Fred", "Bloggs");

        assertThat(student.toString(), is("StudentToString{age='23',firstName='Fred',lastName='Bloggs'}"));
    }

    @Test
    public void shouldAddToStringMethodUsingAllFieldsPlusAdditionalProperties() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/student-to-string-additional-properties-true.json");
        final String packageName = "uk.gov.justice.pojo.string";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Class<?> studentClass = newClasses.get(0);

        final Object student = classInstantiator.newInstance(studentClass, 23, "Fred", "Bloggs");
        final Method additionalPropertySetter = studentClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
        additionalPropertySetter.invoke(student, "name", "value");

        assertThat(student.toString(), is("StudentToStringAdditionalPropertiesTrue{age='23',firstName='Fred',lastName='Bloggs',additionalProperties='{name=value}'}"));
    }
}
