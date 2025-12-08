package uk.gov.justice.services.common.converter;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.UUID.randomUUID;
import static javax.json.JsonValue.NULL;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static uk.gov.justice.services.common.converter.JSONObjectValueObfuscator.obfuscated;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

import java.math.BigDecimal;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

public class JSONObjectValueObfuscatorTest {

    @Test
    public void shouldReplaceStringValues() throws Exception {
        final JsonObject json = getJsonBuilderFactory().createObjectBuilder()
                .add("property1", "Hello World!")
                .add("nested", getJsonBuilderFactory().createObjectBuilder().add("property2", "Hello Universe!")).build();

        with(obfuscated(json)
                .toString())
                .assertThat("property1", is("xxx"))
                .assertThat("nested.property2", is("xxx"));

    }

    @Test
    public void shouldReplaceBooleanValues() throws Exception {
        final JsonObject json = getJsonBuilderFactory().createObjectBuilder()
                .add("property1", true)
                .add("property2", false)
                .add("nested", getJsonBuilderFactory().createObjectBuilder().add("property3", true)).build();

        with(obfuscated(json)
                .toString())
                .assertThat("property1", is(false))
                .assertThat("property2", is(false))
                .assertThat("nested.property3", is(false));
    }

    @Test
    public void shouldReplaceNumericValues() throws Exception {
        final JsonObject json = getJsonBuilderFactory().createObjectBuilder()
                .add("property1", 10)
                .add("property2", 13L)
                .add("nested", getJsonBuilderFactory().createObjectBuilder().add("property3", BigDecimal.valueOf(11111))).build();

        with(obfuscated(json)
                .toString())
                .assertThat("property1", is(0))
                .assertThat("property2", is(0))
                .assertThat("nested.property3", is(0));
    }

    @Test
    public void shouldReplaceUUIDs() throws Exception {
        final JsonObject json = getJsonBuilderFactory().createObjectBuilder()
                .add("property1", randomUUID().toString())
                .add("nested", getJsonBuilderFactory().createObjectBuilder().add("property2", randomUUID().toString())).build();

        with(obfuscated(json)
                .toString())
                .assertThat("property1", is("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"))
                .assertThat("nested.property2", is("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"));
    }


    @Test
    public void shouldReplaceValuesInArray() throws Exception {
        final JsonArray array = getJsonBuilderFactory().createArrayBuilder()
                .add("value1")
                .add("value2")
                .add("value2")
                .add(randomUUID().toString())
                .build();

        final JsonArray array2 = getJsonBuilderFactory().createArrayBuilder()
                .add(1)
                .add(BigDecimal.valueOf(3333))
                .add(77777L)
                .build();

        final JsonObject json = getJsonBuilderFactory().createObjectBuilder()
                .add("property1", array)
                .add("nested", getJsonBuilderFactory().createObjectBuilder().add("property2", array2)).build();

        with(obfuscated(json)
                .toString())
                .assertThat("property1", hasItems("xxx", "xxx", "xxx", "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"))
                .assertThat("nested.property2", hasItems(0, 0, 0));
    }

    @Test
    public void shouldObfuscateJsonObjectInArray() throws Exception {
        final JsonObject object = getJsonBuilderFactory().createObjectBuilder()
                .add("property2", "someValueA").build();

        final JsonObject object2 = getJsonBuilderFactory().createObjectBuilder()
                .add("property3", "someValueB").build();

        final JsonArray array = getJsonBuilderFactory().createArrayBuilder()
                .add(object)
                .add(object2)
                .build();

        final JsonObject json = getJsonBuilderFactory().createObjectBuilder()
                .add("property1", array).build();

        with(obfuscated(json)
                .toString())
                .assertThat("property1[0].property2", is("xxx"))
                .assertThat("property1[1].property3", is("xxx"));
    }

    @Test
    public void shouldReplaceNullValues() throws Exception {
        final JsonObject json = getJsonBuilderFactory().createObjectBuilder()
                .add("property1", NULL)
                .add("nested", getJsonBuilderFactory().createObjectBuilder().add("property2", NULL)).build();

        with(obfuscated(json)
                .toString())
                .assertThat("property1", is(nullValue()))
                .assertThat("nested.property2", is(nullValue()));

    }

}