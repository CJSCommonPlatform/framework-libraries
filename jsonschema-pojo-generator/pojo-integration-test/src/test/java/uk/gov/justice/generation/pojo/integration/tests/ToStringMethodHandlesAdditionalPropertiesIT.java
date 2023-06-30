package uk.gov.justice.generation.pojo.integration.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddToStringMethodToClassPlugin.newAddToStringMethodToClassPlugin;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToStringMethodHandlesAdditionalPropertiesIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @BeforeEach
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/tests/to/string");
    }

    @Test
    public void shouldAddToStringMethodUsingAllFieldsPlusAdditionalProperties() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/tests/student-to-string-additional-properties-true.json");
        final String packageName = "uk.gov.justice.pojo.string";


        final List<Class<?>> newClasses = generatorUtil
                .withClassModifyingPlugin(newAddToStringMethodToClassPlugin())
                .withClassModifyingPlugin(new AddAdditionalPropertiesToClassPlugin())
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Class<?> studentClass = newClasses.get(0);

        final Map<String, Object> additionalPropertyMap = new HashMap<>();
        additionalPropertyMap.put("additionalPropertyName", "additionalPropertyValue");

        final Object student = classInstantiator.newInstance(studentClass, 23, "Fred", "Bloggs", additionalPropertyMap);

        assertThat(student.toString(), is("StudentToStringAdditionalPropertiesTrue{age='23',firstName='Fred',lastName='Bloggs',additionalProperties='{additionalPropertyName=additionalPropertyValue}'}"));
    }
}
