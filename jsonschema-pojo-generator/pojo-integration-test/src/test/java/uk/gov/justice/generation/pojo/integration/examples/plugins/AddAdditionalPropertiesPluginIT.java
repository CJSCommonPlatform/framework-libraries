package uk.gov.justice.generation.pojo.integration.examples.plugins;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;


public class AddAdditionalPropertiesPluginIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new ClasspathFileResource().getFileFromClasspath("/schemas/examples/plugins/add-additional-properties-plugin.json");

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

        final Map<String, Object> additionalPropertyMap = new HashMap<>();
        additionalPropertyMap.put("additionalPropertyName", "additionalPropertyValue");
        additionalPropertyMap.put("additionalPropertyNumber", 1);
        additionalPropertyMap.put("additionalPropertyBoolean", Boolean.TRUE);


        final Object additionalProperties = classInstantiator.newInstance(additionalPropertiesClass, firstName, lastName, additionalPropertyMap);

        final String json = objectMapper.writeValueAsString(additionalProperties);

        with(json)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.additionalPropertyName", is("additionalPropertyValue"))
                .assertThat("$.additionalPropertyNumber", is(1))
                .assertThat("$.additionalPropertyBoolean", is(Boolean.TRUE))
        ;

        generatorUtil.validate(JSON_SCHEMA_FILE, json);
    }


    @Test
    public void shouldWriteObjectWithAdditionalProperties() throws Exception {

        final String jsonString = "{\"name\":\"Jack\",\"age\":42,\"Test\":\"Test Value\",\"Test 2\":\"Test Value 2\",\"Test Number\":25}";
        final PersonWithAdditionalProperties personWithAdditionalProperties = objectMapper.readValue(jsonString, PersonWithAdditionalProperties.class);

        assertThat(personWithAdditionalProperties.getName(), is("Jack"));
        assertThat(personWithAdditionalProperties.getAge(), is(42));
        assertThat(personWithAdditionalProperties.getAdditionalProperties(), is(notNullValue()));
        assertThat(personWithAdditionalProperties.getAdditionalProperties().get("Test"), is("Test Value"));
        assertThat(personWithAdditionalProperties.getAdditionalProperties().get("Test 2"), is("Test Value 2"));
        assertThat(personWithAdditionalProperties.getAdditionalProperties().get("Test Number"), is(25));
    }

    private static class PersonWithAdditionalProperties {

        private String name;
        private int age;
        private Map<String, Object> additionalProperties;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }
    }
}
