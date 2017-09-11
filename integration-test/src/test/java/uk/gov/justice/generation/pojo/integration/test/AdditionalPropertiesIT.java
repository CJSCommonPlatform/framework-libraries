package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class AdditionalPropertiesIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();


    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/additional-properties");
    }

    @Test
    public void shouldGenerateAClassWithAMapForAdditionalPropertiesIfAdditionalPropertiesIsTrue() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/additional-properties.json");
        final String packageName = "uk.gov.justice.pojo.additional.properties";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Class<?> additionalPropertiesClass = newClasses.get(0);

        final String firstName = "firstName";
        final String lastName = "lastName";

        final Object additionalProperties = classInstantiator.newInstance(additionalPropertiesClass, firstName, lastName);
        final Method additionalPropertySetter = additionalPropertiesClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
        additionalPropertySetter.invoke(additionalProperties, "additionalPropertyName", "additionalPropertyValue");

        final String json = objectMapper.writeValueAsString(additionalProperties);

        with(json)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.additionalPropertyName", is("additionalPropertyValue"))
        ;

        generatorUtil.validate(jsonSchemaFile, json);
    }
}
