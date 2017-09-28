package uk.gov.justice.generation.pojo.integration.examples.plugins;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class AddAdditionalPropertiesPluginIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new File("src/test/resources/schemas/examples/plugins/add-additional-properties-plugin.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/plugins/add-additional-properties-plugin");
    }

    @Test
    public void shouldGenerateClassWithMapForAdditionalPropertiesIfAdditionalPropertiesIsTrue() throws Exception {

        final String packageName = "uk.gov.justice.pojo.additional.properties";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("PersonWithAdditionalProperties")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .withClassModifyingPlugin(new AddAdditionalPropertiesToClassPlugin())
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
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

        generatorUtil.validate(JSON_SCHEMA_FILE, json);
    }
}
