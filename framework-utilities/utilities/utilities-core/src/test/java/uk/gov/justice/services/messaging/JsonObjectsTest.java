package uk.gov.justice.services.messaging;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static javax.json.JsonValue.NULL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static uk.gov.justice.services.messaging.JsonObjects.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link JsonObjects} class.
 */
public class JsonObjectsTest {

    private static final String UUID_A = "da45e8f6-d945-4f09-a115-1139a9dbb754";
    private static final String UUID_B = "d04885b4-9652-4c2a-87c6-299bda0a87d4";

    private static final javax.json.JsonBuilderFactory jsonBuilderFactory = JsonObjects.getJsonBuilderFactory();
    private static final javax.json.JsonReaderFactory jsonReaderFactory = JsonObjects.getJsonReaderFactory();

    @Test
    public void shouldReturnJsonArray() {
        JsonArray array = jsonBuilderFactory.createArrayBuilder()
                .addNull()
                .build();
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", array)
                .build();
        Optional<JsonArray> jsonArray = getJsonArray(object, "name");

        assertThat(jsonArray.isPresent(), is(true));
        assertThat(jsonArray.get(), equalTo(array));
    }

    @Test
    public void shouldReturnJsonObject() {
        JsonObject subObject = jsonBuilderFactory.createObjectBuilder()
                .add("name2", "cheese")
                .build();
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", subObject)
                .build();
        Optional<JsonObject> jsonObject = getJsonObject(object, "name");

        assertThat(jsonObject.isPresent(), is(true));
        assertThat(jsonObject.get(), equalTo(subObject));
    }

    @Test
    public void shouldReturnJsonNumber() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", 99L)
                .build();
        Optional<JsonNumber> jsonNumber = getJsonNumber(object, "name");

        assertThat(jsonNumber.isPresent(), is(true));
        assertThat(jsonNumber.get().longValue(), equalTo(99L));
    }

    @Test
    public void shouldReturnJsonString() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", "test")
                .build();
        Optional<JsonString> jsonString = getJsonString(object, "name");

        assertThat(jsonString.isPresent(), is(true));
        assertThat(jsonString.get().getString(), equalTo("test"));
    }

    @Test
    public void shouldReturnJsonStringForNestedField() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", jsonBuilderFactory.createObjectBuilder()
                        .add("name2", "test")
                        .build())
                .build();
        Optional<JsonString> jsonString = getJsonString(object, "name", "name2");

        assertThat(jsonString.isPresent(), is(true));
        assertThat(jsonString.get().getString(), equalTo("test"));
    }

    @Test
    public void shouldReturnString() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", "test")
                .build();
        Optional<String> string = getString(object, "name");

        assertThat(string.isPresent(), is(true));
        assertThat(string.get(), equalTo("test"));
    }

    @Test
    public void shouldReturnBoolean() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("someBoolean", true)
                .build();
        Optional<Boolean> someBoolean = getBoolean(object, "someBoolean");

        assertThat(someBoolean.isPresent(), is(true));
        assertThat(someBoolean.get(), is(true));
    }


    @Test
    public void shouldReturnEmptyIfBooleanFieldUnknown() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .build();
        Optional<Boolean> someBoolean = getBoolean(object, "someBoolean");

        assertThat(someBoolean.isPresent(), is(false));
    }

    @Test
    public void shouldThrowExceptionForNonBoolean() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("someBool", 99L)
                .build();
        assertThrows(IllegalStateException.class, () -> getBoolean(object, "someBool"));
    }

    @Test
    public void shouldThrowExceptionForNonString() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", 99L)
                .build();
        assertThrows(IllegalStateException.class, () -> getString(object, "name"));
    }

    @Test
    public void shouldReturnUUID() {
        final String stringValue = "6c84963d-47a1-4d57-a706-09bea3fa84a5";
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", stringValue)
                .build();
        Optional<UUID> uuid = getUUID(object, "name");

        assertThat(uuid.isPresent(), is(true));
        assertThat(uuid.get(), equalTo(UUID.fromString(stringValue)));
    }

    @Test
    public void shouldThrowExceptionForNonUUID() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", "blah")
                .build();
        assertThrows(IllegalStateException.class, () -> getUUID(object, "name"));
    }

    @Test
    public void shouldReturnLong() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", 99L)
                .build();
        Optional<Long> string = getLong(object, "name");

        assertThat(string.isPresent(), is(true));
        assertThat(string.get(), equalTo(99L));
    }

    @Test
    public void shouldThrowExceptionForNonLong() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", "blah")
                .build();
        assertThrows(IllegalStateException.class, () -> getLong(object, "name"));
    }

    @Test
    public void shouldReturnListOfJsonStrings() {
        JsonArray array = jsonBuilderFactory.createArrayBuilder()
                .add("test1")
                .add("test2")
                .build();
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", array)
                .build();
        Optional<List<JsonString>> jsonStrings = getList(object, JsonString.class, "name");

        assertThat(jsonStrings.isPresent(), is(true));
        assertThat(jsonStrings.get(), hasSize(2));
        assertThat(jsonStrings.get().get(0).getString(), equalTo("test1"));
        assertThat(jsonStrings.get().get(1).getString(), equalTo("test2"));
    }

    @Test
    public void shouldReturnListOfStrings() {
        JsonArray array = jsonBuilderFactory.createArrayBuilder()
                .add("test1")
                .add("test2")
                .build();
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", array)
                .build();
        Optional<List<String>> strings = getList(object, JsonString.class, JsonString::getString, "name");

        assertThat(strings.isPresent(), is(true));
        assertThat(strings.get(), equalTo(ImmutableList.of("test1", "test2")));
    }

    @Test
    public void shouldReturnListOfUUIDs() {
        JsonArray array = jsonBuilderFactory.createArrayBuilder()
                .add(UUID_A)
                .add(UUID_B)
                .build();
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", array)
                .build();
        List<UUID> uuids = getUUIDs(object, "name");

        assertThat(uuids, equalTo(ImmutableList.of(UUID.fromString(UUID_A), UUID.fromString(UUID_B))));
    }

    @Test
    public void shouldThrowExceptionForNullObject() {

        assertThrows(IllegalArgumentException.class, () -> getString(null, "name"));
    }

    @Test
    public void shouldThrowExceptionForNullFieldName() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", 99L)
                .build();
        assertThrows(IllegalArgumentException.class, () -> getString(object, (String) null));
    }

    @Test
    public void shouldThrowExceptionForEmptyVarArgs() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", 99L)
                .build();
        assertThrows(IllegalArgumentException.class, () -> getString(object));
    }

    @Test
    public void shouldReturnEmptyIfFieldIsUnknown() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", "test")
                .build();
        Optional<String> string = getString(object, "name2");

        assertThat(string.isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyIfFieldValueIsNull() {
        JsonObject object = jsonBuilderFactory.createObjectBuilder()
                .add("name", NULL)
                .build();
        Optional<String> string = getString(object, "name");

        assertThat(string.isPresent(), is(false));
    }

    @Test
    public void shouldCreateBuilderFromJsonObject() {
        JsonObject source = jsonBuilderFactory.createObjectBuilder()
                .add("name", "test")
                .build();

        JsonObjectBuilder builder = createObjectBuilder(source);

        assertThat(builder.build(), equalTo(source));
    }

    @Test
    public void shouldCreateBuilderFromJsonObjectWithFilter() {
        JsonObject source = jsonBuilderFactory.createObjectBuilder()
                .add("id", "test id")
                .add("ignore1", "ignore this")
                .add("name", "test")
                .add("ignore2", "ignore this as well")
                .build();

        JsonObjectBuilder builder = createObjectBuilderWithFilter(source, x -> !"ignore1".equals(x) && !"ignore2".equals(x));

        JsonObject actual = builder.build();
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.getString("id"), equalTo(source.getString("id")));
        assertThat(actual.getString("name"), equalTo(source.getString("name")));
    }

    @Test
    public void shouldConvertCollectionOfJsonObjectsToJsonArray() {

        final String oldKey = "oldKey";
        final String newKey = "newKey";

        final JsonArray input = jsonBuilderFactory.createArrayBuilder()
                .add(jsonBuilderFactory.createObjectBuilder().add(oldKey, "value1"))
                .add(jsonBuilderFactory.createObjectBuilder().add(oldKey, "value2"))
                .add(jsonBuilderFactory.createObjectBuilder().add(oldKey, "value3"))
                .build();

        final Function<JsonObject, JsonValue> converter = source -> jsonBuilderFactory.createObjectBuilder()
                .add(newKey, source.getString(oldKey))
                .build();

        final JsonArray actual = toJsonArray(input.getValuesAs(JsonObject.class), converter);

        final JsonArray expected = jsonBuilderFactory.createArrayBuilder()
                .add(jsonBuilderFactory.createObjectBuilder().add(newKey, "value1"))
                .add(jsonBuilderFactory.createObjectBuilder().add(newKey, "value2"))
                .add(jsonBuilderFactory.createObjectBuilder().add(newKey, "value3"))
                .build();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void shouldConvertCollectionOfObjectsToJsonArray() {
        final String key = "key";
        final String value = "TEST";

        final Function<String, JsonValue> converter = source -> jsonBuilderFactory.createObjectBuilder().add(key, source).build();

        final JsonArray result = toJsonArray(asList(value), converter);

        final JsonArray expected = jsonBuilderFactory.createArrayBuilder()
                .add(jsonBuilderFactory.createObjectBuilder().add(key, value))
                .build();

        assertThat(result, equalTo(expected));

    }

    @Test
    public void shouldConvertEmptyCollectionToJsonArray() {

        final JsonArray result = toJsonArray(emptyList(), source -> jsonBuilderFactory.createObjectBuilder().build());

        final JsonArray expected = jsonBuilderFactory.createArrayBuilder().build();

        assertThat(result, equalTo(expected));
    }

    @Test
    public void shouldJsonObjectsCacheProviders() {
        assertNotNull(jsonBuilderFactory);
        assertTrue(jsonBuilderFactory.getConfigInUse().isEmpty());
        assertNotNull(jsonReaderFactory);
        assertTrue(jsonReaderFactory.getConfigInUse().isEmpty());
    }

    @Test
    public void shouldGetJsonReaderFactoryAndCacheIt() {
        // when
        final javax.json.JsonReaderFactory first = JsonObjects.getJsonReaderFactory();
        final javax.json.JsonReaderFactory second = JsonObjects.getJsonReaderFactory();

        // then
        assertNotNull(first);
        assertTrue(first.getConfigInUse().isEmpty());
        assertSame(first, second, "JsonReaderFactory should be a cached singleton instance");
    }

    @Test
    public void shouldGetJsonWriterFactoryAndCacheIt() {
        // when
        final javax.json.JsonWriterFactory first = JsonObjects.getJsonWriterFactory();
        final javax.json.JsonWriterFactory second = JsonObjects.getJsonWriterFactory();

        // then
        assertNotNull(first);
        assertTrue(first.getConfigInUse().isEmpty());
        assertSame(first, second, "JsonWriterFactory should be a cached singleton instance");
    }

    @Test
    public void shouldGetJsonBuilderFactoryAndCacheIt() {
        // when
        final javax.json.JsonBuilderFactory first = JsonObjects.getJsonBuilderFactory();
        final javax.json.JsonBuilderFactory second = JsonObjects.getJsonBuilderFactory();

        // then
        assertNotNull(first);
        assertTrue(first.getConfigInUse().isEmpty());
        assertSame(first, second, "JsonBuilderFactory should be a cached singleton instance");
    }

    @Test
    public void shouldGetProviderAndCacheIt() {
        // when
        final javax.json.spi.JsonProvider first = JsonObjects.getProvider();
        final javax.json.spi.JsonProvider second = JsonObjects.getProvider();

        // then
        assertNotNull(first);
        assertSame(first, second, "JsonProvider should be a cached singleton instance");
    }

    @Test
    public void shouldCreateParserFromReader() {
        // given
        final String json = "{\"a\":1}";

        // when
        final javax.json.stream.JsonParser parser = JsonObjects.createParser(new java.io.StringReader(json));
        boolean sawKey = false;
        boolean sawValue = false;
        while (parser.hasNext()) {
            final javax.json.stream.JsonParser.Event event = parser.next();
            if (event == javax.json.stream.JsonParser.Event.KEY_NAME) {
                sawKey = true;
            }
            if (event == javax.json.stream.JsonParser.Event.VALUE_NUMBER) {
                sawValue = true;
            }
        }

        // then
        assertTrue(sawKey, "Parser should see a KEY_NAME event");
        assertTrue(sawValue, "Parser should see a VALUE_NUMBER event");
    }

    @Test
    public void shouldCreateParserFromInputStream() {
        // given
        final String json = "{\"b\":2}";
        final java.io.InputStream in = new java.io.ByteArrayInputStream(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        // when
        final javax.json.stream.JsonParser parser = JsonObjects.createParser(in);
        boolean sawKey = false;
        boolean sawValue = false;
        while (parser.hasNext()) {
            final javax.json.stream.JsonParser.Event event = parser.next();
            if (event == javax.json.stream.JsonParser.Event.KEY_NAME) {
                sawKey = true;
            }
            if (event == javax.json.stream.JsonParser.Event.VALUE_NUMBER) {
                sawValue = true;
            }
        }

        // then
        assertTrue(sawKey, "Parser should see a KEY_NAME event");
        assertTrue(sawValue, "Parser should see a VALUE_NUMBER event");
    }

    @Test
    public void shouldCreateGeneratorToWriter() {
        // given
        final java.io.StringWriter writer = new java.io.StringWriter();

        // when
        final javax.json.stream.JsonGenerator generator = JsonObjects.createGenerator(writer);
        generator.writeStartObject().write("a", 1).writeEnd();
        generator.close();

        // then
        final String json = writer.toString();
        final javax.json.JsonObject obj = JsonObjects.createReader(new java.io.StringReader(json)).readObject();
        assertThat(obj.getInt("a"), equalTo(1));
    }

    @Test
    public void shouldCreateGeneratorToOutputStream() {
        // given
        final java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

        // when
        final javax.json.stream.JsonGenerator generator = JsonObjects.createGenerator(out);
        generator.writeStartObject().write("b", 2).writeEnd();
        generator.close();

        // then
        final String json = new String(out.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
        final javax.json.JsonObject obj = JsonObjects.createReader(new java.io.StringReader(json)).readObject();
        assertThat(obj.getInt("b"), equalTo(2));
    }

    @Test
    public void shouldCreateWriterAndReaderUsingWriter() {
        // given
        final javax.json.JsonObject source = JsonObjects.createObjectBuilder().add("x", "y").build();

        // when
        final java.io.StringWriter stringWriter = new java.io.StringWriter();
        try (final javax.json.JsonWriter jsonWriter = JsonObjects.createWriter(stringWriter)) {
            jsonWriter.write(source);
        }

        // then
        final String json = stringWriter.toString();
        final javax.json.JsonObject readBack = JsonObjects.createReader(new java.io.StringReader(json)).readObject();
        assertThat(readBack.getString("x"), equalTo("y"));
    }

    @Test
    public void shouldCreateWriterAndReaderUsingOutputStream() {
        // given
        final javax.json.JsonObject source = JsonObjects.createObjectBuilder().add("p", true).build();

        // when
        final java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        try (final javax.json.JsonWriter jsonWriter = JsonObjects.createWriter(out)) {
            jsonWriter.write(source);
        }

        // then
        final byte[] bytes = out.toByteArray();
        final javax.json.JsonObject readBack = JsonObjects.createReader(new java.io.ByteArrayInputStream(bytes)).readObject();
        assertThat(readBack.getBoolean("p"), is(true));
    }

    @Test
    public void shouldCreateArrayBuilder() {
        // when
        final javax.json.JsonArray array = JsonObjects.createArrayBuilder().add("v1").add("v2").build();

        // then
        assertThat(array.size(), equalTo(2));
        assertThat(array.getString(0), equalTo("v1"));
        assertThat(array.getString(1), equalTo("v2"));
    }
}
