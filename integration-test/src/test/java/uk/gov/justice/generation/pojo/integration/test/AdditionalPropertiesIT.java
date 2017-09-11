package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class AdditionalPropertiesIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/additional-properties");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldGenerateAClassWithAMapForAdditionalPropertiesIfAdditionalPropertiesIsTrue() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/additional-properties.json");
        final String packageName = "uk.gov.justice.pojo.additional.properties";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath());

        assertThat(newClasses.size(), is(1));

        final Class<?> additionalPropertiesClass = newClasses.get(0);

        final String firstName = "firstName";
        final String lastName = "lastName";

        final Constructor<?> additionalPropertiesConstructor = additionalPropertiesClass
                .getConstructor(String.class, String.class);

        final Object additionalProperties = additionalPropertiesConstructor.newInstance(firstName, lastName);
        final Method additionalPropertySetter = additionalPropertiesClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
        additionalPropertySetter.invoke(additionalProperties, "additionalPropertyName", "additionalPropertyValue");

        final String json = objectMapper.writeValueAsString(additionalProperties);

        with(json)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.additionalPropertyName", is("additionalPropertyValue"))
        ;
    }
}
