package uk.gov.justice.services.common.converter;

import static com.jayway.jsonassert.JsonAssert.with;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static uk.gov.justice.services.common.converter.ObjectMapperProducerTest.Colour.BLUE;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.util.Objects;

import javax.json.JsonObject;
import javax.json.JsonValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class ObjectMapperProducerTest {

    private static final String JSON_OBJECT_STRING = "{\n" +
            "  \"id\": \"861c9430-7bc6-4bf0-b549-6534394b8d65\"\n" +
            "}";

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

    public static enum Colour {
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
}
