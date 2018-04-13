package uk.gov.justice.services.common.converter;

import static com.jayway.jsonassert.JsonAssert.with;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static uk.gov.justice.services.common.converter.ObjectMapperProducerTest.Colour.BLUE;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.json.JsonObject;
import javax.json.JsonValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ObjectMapperProducerTest {

    private static final String JSON_OBJECT_STRING = "{\n" +
            "  \"id\": \"861c9430-7bc6-4bf0-b549-6534394b8d65\"\n" +
            "}";
    private static final String YAML_AS_STRING = "---\n" +
            "subscription_descriptor:\n" +
            "  spec_version: 1.0.0\n";

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapperProducer().objectMapper();
    }

    @Test
    public void shouldReturnAMapper() throws Exception {
        assertThat(mapper, notNullValue());
        assertThat(mapper, isA(ObjectMapper.class));
    }

    @Test
    public void shouldOmitsNullValuesFromObjects() throws Exception {
        final Object source = new Object() {
            public String getId() {
                return "861c9430-7bc6-4bf0-b549-6534394b8d65";
            }

            public String getName() {
                return null;
            }
        };

        final String json = mapper.writeValueAsString(source);

        assertEquals(JSON_OBJECT_STRING, json, true);
    }

    @Test
    public void shouldOmitsNullValuesFromJsonObjects() throws Exception {
        final JsonObject source = createObjectBuilder()
                .add("id", "861c9430-7bc6-4bf0-b549-6534394b8d65")
                .add("name", JsonValue.NULL)
                .build();

        final String json = mapper.writeValueAsString(source);

        assertEquals(JSON_OBJECT_STRING, json, true);
    }

    @Test
    public void shouldBeAbleToSerializeSingleArgConstructor() throws Exception {

        final DummyBeanWithSingleArgConstructor bean = new DummyBeanWithSingleArgConstructor("fred");

        final String json = mapper.writeValueAsString(bean);

        assertThat(mapper.readValue(json, DummyBeanWithSingleArgConstructor.class), is(bean));

    }

    @Test
    public void shouldParseAnEnumIntoJson() throws Exception {

        final String name = "Fred";
        final int age = 42;
        final Colour favouriteColour = BLUE;

        final Person fred = new Person(name, age, favouriteColour);

        final String json = mapper.writeValueAsString(fred);

        with(json)
                .assertThat("$.name", is(name))
                .assertThat("age", is(age))
                .assertThat("$.favouriteColour", is("Blue"))
        ;
    }

    @Test
    public void shouldParseJsonIntoAnEnum() throws Exception {

        final String name = "Fred";
        final int age = 42;
        final Colour favouriteColour = BLUE;

        final String json = "{\"name\":\"Fred\",\"age\":42,\"favouriteColour\":\"Blue\"}";

        final Person person = mapper.readValue(json, Person.class);

        assertThat(person.getName(), is(name));
        assertThat(person.getAge(), is(age));
        assertThat(person.getFavouriteColour(), is(favouriteColour));
    }

    @Test
    public void shouldReadObjectWithAdditionalProperties() throws Exception {

        final Map<String, Object> additionalProperties = Collections.singletonMap("additionalPropertiesKey", "additionalPropertyValue");

        final PersonWithAdditionalProperties bean = new PersonWithAdditionalProperties("fred", 42, additionalProperties);

        final String json = mapper.writeValueAsString(bean);

        with(json)
                .assertThat("$.name", is("fred"))
                .assertThat("$.age", is(42))
                .assertThat("$.additionalPropertiesKey", is("additionalPropertyValue"));

    }

    @Test
    public void shouldWriteObjectWithAdditionalProperties() throws Exception {

        final String jsonString = "{\"name\":\"Jack\",\"age\":42,\"Test\":\"Test Value\",\"Test 2\":\"Test Value 2\",\"Test Number\":25}";
        final PersonWithAdditionalProperties personWithAdditionalProperties = mapper.readValue(jsonString, PersonWithAdditionalProperties.class);

        assertThat(personWithAdditionalProperties.getName(), is("Jack"));
        assertThat(personWithAdditionalProperties.getAge(), is(42));
        assertThat(personWithAdditionalProperties.getAdditionalProperties(), is(notNullValue()));
        assertThat(personWithAdditionalProperties.getAdditionalProperties().get("Test"), is("Test Value"));
        assertThat(personWithAdditionalProperties.getAdditionalProperties().get("Test 2"), is("Test Value 2"));
        assertThat(personWithAdditionalProperties.getAdditionalProperties().get("Test Number"), is(25));
    }

    @Test
    public void shouldReadAdditionalPropertiesWithAllPropertyTypes() throws Exception {

        final String json = "{\n" +
                "  \"booleanFalseProperty\": false,\n" +
                "  \"nullProperty\": null,\n" +
                "  \"intProperty\": 23,\n" +
                "  \"booleanTrueProperty\": true,\n" +
                "  \"stringProperty\": \"a String\",\n" +
                "  \"floatProperty\": 23.23,\n" +
                "  \"name\": \"a name\",\n" +
                "  \"age\": 23\n" +
                "}\n";

        final PersonWithAdditionalProperties person = mapper.readValue(json, PersonWithAdditionalProperties.class);

        assertThat(person.getAdditionalProperties().get("booleanFalseProperty"), is(false));
        assertThat(person.getAdditionalProperties().get("nullProperty"), is(nullValue()));
        assertThat(person.getAdditionalProperties().get("intProperty"), is(23));
        assertThat(person.getAdditionalProperties().get("booleanTrueProperty"), is(true));
        assertThat(person.getAdditionalProperties().get("stringProperty"), is("a String"));
        assertThat(person.getAdditionalProperties().get("floatProperty"), is(23.23F));
    }

    @Test
    public void shouldWriteAdditionalPropertiesWithAllPropertyTypes() throws Exception {

        final HashMap<String, Object> additionalProperties = new HashMap<>();

        final String stringProperty = "a String";
        final int intProperty = 23;
        final double floatProperty = 23.23;
        final boolean booleanTrueProperty = true;
        final boolean booleanFalseProperty = false;
        final Object nullProperty = null;

        additionalProperties.put("stringProperty", stringProperty);
        additionalProperties.put("intProperty", intProperty);
        additionalProperties.put("floatProperty", floatProperty);
        additionalProperties.put("booleanTrueProperty", booleanTrueProperty);
        additionalProperties.put("booleanFalseProperty", booleanFalseProperty);
        additionalProperties.put("nullProperty", nullProperty);

        final PersonWithAdditionalProperties person = new PersonWithAdditionalProperties(
                "a name",
                23,
                additionalProperties
        );

        final String json = mapper.writeValueAsString(person);

        with(json)
                .assertThat("$.stringProperty", is(stringProperty))
                .assertThat("$.intProperty", is(intProperty))
                .assertThat("$.floatProperty", is(floatProperty))
                .assertThat("$.booleanTrueProperty", is(booleanTrueProperty))
                .assertThat("$.booleanFalseProperty", is(booleanFalseProperty))
                .assertThat("$.nullProperty", is(nullValue()))
        ;
    }

    @Test
    public void shouldConvertYamlToJsonObject() throws Exception {
        final ObjectMapper yamlObjectMapper = new ObjectMapperProducer().objectMapperWith(new YAMLFactory());

        final Object yamlObject = yamlObjectMapper.readValue(YAML_AS_STRING, Object.class);
        final JSONObject yamlAsJsonObject = new JSONObject(mapper.writeValueAsString(yamlObject));

        assertThat(yamlAsJsonObject.getJSONObject("subscription_descriptor").get("spec_version"), is("1.0.0"));
    }

    public static class DummyBeanWithSingleArgConstructor {
        private final String name;

        private DummyBeanWithSingleArgConstructor(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final DummyBeanWithSingleArgConstructor that = (DummyBeanWithSingleArgConstructor) o;
            return Objects.equals(getName(), that.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName());
        }
    }

    public enum Colour {
        RED("Red"),
        GREEN("Green"),
        BLUE("Blue");

        private final String name;

        Colour(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class Person {

        private final String name;
        private final int age;
        private final Colour favouriteColour;

        public Person(final String name, final int age, final Colour favouriteColour) {
            this.name = name;
            this.age = age;
            this.favouriteColour = favouriteColour;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Colour getFavouriteColour() {
            return favouriteColour;
        }
    }

    public static class PersonWithAdditionalProperties {

        private String name;
        private int age;
        private Map<String, Object> additionalProperties;


        public PersonWithAdditionalProperties(final String name, final int age, final Map<String, Object> additionalProperties) {
            this.name = name;
            this.age = age;
            this.additionalProperties = additionalProperties;
        }

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
