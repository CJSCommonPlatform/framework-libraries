package uk.gov.justice.services.messaging;

import static jakarta.json.JsonValue.ValueType;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonException;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;

import com.google.common.collect.ImmutableList;

/**
 * Static utility methods for reading deeply-nested values from a {@link JsonObject}, and for
 * creating JSON parsers, generators, readers, writers, and builders.
 *
 * <p>All Jakarta JSON-P factory objects ({@link JsonProvider}, {@link JsonBuilderFactory},
 * {@link JsonReaderFactory}, {@link JsonWriterFactory}) are resolved once at class-load time and
 * cached as static fields. Callers should use the methods on this class rather than calling
 * {@code Json.create*()} directly, both to avoid repeated factory-creation overhead and to
 * ensure the correct {@link JsonProvider} is used in Maven plugin classloader contexts.
 */
public final class JsonObjects {
    private static final JsonProvider provider = findProvider();
    private static final JsonBuilderFactory jsonBuilderFactory = provider.createBuilderFactory(null);
    private static final JsonReaderFactory jsonReaderFactory = provider.createReaderFactory(null);
    private static final JsonWriterFactory jsonWriterFactory = provider.createWriterFactory(null);


    private static final String FIELD_IS_NOT_A_TYPE = "Field %s is not a %s";

    private static JsonProvider findProvider() {
        // Use the classloader that loaded JsonProvider itself (the plugin realm classloader in
        // Maven plugin contexts) rather than the Thread Context ClassLoader used by
        // JsonProvider.provider(). This avoids isAssignableFrom mismatches caused by Classworlds
        // realm isolation, where the TCCL resolves the implementation through a different realm
        // than the one that loaded the JsonProvider interface.
        return ServiceLoader.load(JsonProvider.class, JsonProvider.class.getClassLoader())
                .findFirst()
                .orElseGet(JsonProvider::provider);
    }

    /**
     * Private constructor to prevent misuse of utility class.
     */
    private JsonObjects() {
    }

    /**
     * Returns the cached {@link JsonProvider}.
     *
     * <p>The provider is resolved once at class-load time using a classloader-aware
     * {@link ServiceLoader} call and reused for all subsequent operations.
     *
     * @return the cached {@link JsonProvider} instance
     */
    public static JsonProvider getProvider() {
        return provider;
    }

    /**
     * Returns the cached {@link JsonBuilderFactory}.
     *
     * <p>Equivalent to {@code Json.createBuilderFactory(null)} but resolved once at
     * class-load time and reused, avoiding repeated factory-creation overhead.
     *
     * @return the cached {@link JsonBuilderFactory} instance
     */
    public static JsonBuilderFactory getJsonBuilderFactory() {
        return jsonBuilderFactory;
    }

    /**
     * Returns the cached {@link JsonReaderFactory}.
     *
     * <p>Equivalent to {@code Json.createReaderFactory(null)} but resolved once at
     * class-load time and reused, avoiding repeated factory-creation overhead.
     *
     * @return the cached {@link JsonReaderFactory} instance
     */
    public static JsonReaderFactory getJsonReaderFactory() {
        return jsonReaderFactory;
    }

    /**
     * Returns the cached {@link JsonWriterFactory}.
     *
     * <p>Equivalent to {@code Json.createWriterFactory(null)} but resolved once at
     * class-load time and reused, avoiding repeated factory-creation overhead.
     *
     * @return the cached {@link JsonWriterFactory} instance
     */
    public static JsonWriterFactory getJsonWriterFactory() {
        return jsonWriterFactory;
    }

    /**
     * Creates a {@link JsonParser} that reads from the given {@link Reader}, using the
     * cached {@link JsonProvider}.
     *
     * @param reader the character stream from which JSON is to be read
     * @return a {@link JsonParser} positioned at the start of the JSON text
     */
    public static JsonParser createParser(Reader reader) {
        return getProvider().createParser(reader);
    }

    /**
     * Creates a {@link JsonParser} that reads from the given {@link InputStream}, using the
     * cached {@link JsonProvider}. The character encoding is auto-detected per the JSON
     * specification.
     *
     * @param in the byte stream from which JSON is to be read
     * @return a {@link JsonParser} positioned at the start of the JSON text
     * @throws JsonException if the character encoding cannot be determined, or if an I/O error
     *                       occurs (the causing {@link java.io.IOException} will be the cause)
     */
    public static JsonParser createParser(InputStream in) {
        return getProvider().createParser(in);
    }

    /**
     * Creates a {@link JsonGenerator} that writes to the given {@link Writer}, using the
     * cached {@link JsonProvider}.
     *
     * @param writer the character stream to which JSON is written
     * @return a new {@link JsonGenerator}
     */
    public static JsonGenerator createGenerator(Writer writer) {
        return getProvider().createGenerator(writer);
    }

    /**
     * Creates a {@link JsonGenerator} that writes to the given {@link OutputStream}, using the
     * cached {@link JsonProvider}.
     *
     * @param out the byte stream to which JSON is written
     * @return a new {@link JsonGenerator}
     */
    public static JsonGenerator createGenerator(OutputStream out) {
        return getProvider().createGenerator(out);
    }

    /**
     * Creates a {@link JsonWriter} that writes to the given {@link Writer}, using the
     * cached {@link JsonWriterFactory}.
     *
     * @param writer the character stream to which a JSON object or array is written
     * @return a new {@link JsonWriter}
     */
    public static JsonWriter createWriter(Writer writer) {
        return getJsonWriterFactory().createWriter(writer);
    }

    /**
     * Creates a {@link JsonWriter} that writes to the given {@link OutputStream}, using the
     * cached {@link JsonWriterFactory}.
     *
     * @param out the byte stream to which a JSON object or array is written
     * @return a new {@link JsonWriter}
     */
    public static JsonWriter createWriter(OutputStream out) {
        return getJsonWriterFactory().createWriter(out);
    }

    /**
     * Creates a {@link JsonReader} that reads from the given {@link Reader}, using the
     * cached {@link JsonReaderFactory}.
     *
     * @param reader the character stream from which JSON is to be read
     * @return a new {@link JsonReader}
     */
    public static JsonReader createReader(Reader reader) {
        return getJsonReaderFactory().createReader(reader);
    }

    /**
     * Creates a {@link JsonReader} that reads from the given {@link InputStream}, using the
     * cached {@link JsonReaderFactory}.
     *
     * @param in the byte stream from which JSON is to be read
     * @return a new {@link JsonReader}
     */
    public static JsonReader createReader(InputStream in) {
        return getJsonReaderFactory().createReader(in);
    }

    /**
     * Creates a new {@link JsonArrayBuilder} using the cached {@link JsonBuilderFactory}.
     *
     * @return a new {@link JsonArrayBuilder}
     */
    public static JsonArrayBuilder createArrayBuilder() {
        return getJsonBuilderFactory().createArrayBuilder();
    }

    /**
     * Creates a new empty {@link JsonObjectBuilder} using the cached {@link JsonBuilderFactory}.
     *
     * @return a new {@link JsonObjectBuilder}
     */
    public static JsonObjectBuilder createObjectBuilder() {
        return getJsonBuilderFactory().createObjectBuilder();
    }

    /**
     * Returns the (possibly nested) array value at the given field name path, if it exists and
     * is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param names  one or more field names forming a path into nested objects
     * @return an {@link Optional} containing the {@link JsonArray}, or {@link Optional#empty()}
     *         if the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON array
     */
    public static Optional<JsonArray> getJsonArray(final JsonObject object, final String... names) {
        return getJsonValue(object, ValueType.ARRAY, JsonObject::getJsonArray, names);
    }

    /**
     * Returns the (possibly nested) object value at the given field name path, if it exists and
     * is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param names  one or more field names forming a path into nested objects
     * @return an {@link Optional} containing the {@link JsonObject}, or {@link Optional#empty()}
     *         if the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON object
     */
    public static Optional<JsonObject> getJsonObject(final JsonObject object, final String... names) {
        return getJsonValue(object, ValueType.OBJECT, JsonObject::getJsonObject, names);
    }

    /**
     * Returns the (possibly nested) number value at the given field name path, if it exists and
     * is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param names  one or more field names forming a path into nested objects
     * @return an {@link Optional} containing the {@link JsonNumber}, or {@link Optional#empty()}
     *         if the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON number
     */
    public static Optional<JsonNumber> getJsonNumber(final JsonObject object, final String... names) {
        return getJsonValue(object, ValueType.NUMBER, JsonObject::getJsonNumber, names);
    }

    /**
     * Returns the (possibly nested) string value at the given field name path, if it exists and
     * is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param names  one or more field names forming a path into nested objects
     * @return an {@link Optional} containing the {@link JsonString}, or {@link Optional#empty()}
     *         if the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON string
     */
    public static Optional<JsonString> getJsonString(final JsonObject object, final String... names) {
        return getJsonValue(object, ValueType.STRING, JsonObject::getJsonString, names);
    }

    /**
     * Retrieves a typed JSON value at a (possibly nested) field path, applying type checking.
     *
     * <p>If {@code names} contains a single element the value is looked up directly in
     * {@code object}. If it contains more than one element, the first name is resolved as an
     * intermediate {@link JsonObject} and the lookup recurses into it with the remaining names.
     *
     * @param object    the {@link JsonObject} from which to retrieve the value
     * @param valueType the expected {@link ValueType} of the target field
     * @param function  extracts the correctly-typed value from a {@link JsonObject} by field name
     * @param names     one or more field names forming a path into nested objects
     * @param <T>       the {@link JsonValue} subtype to return
     * @return an {@link Optional} containing the value, or {@link Optional#empty()} if the field
     *         is absent at any level of the path, or its value is JSON null
     * @throws IllegalStateException if the field exists but its {@link ValueType} does not match
     *                               {@code valueType}
     */
    private static <T extends JsonValue> Optional<T> getJsonValue(final JsonObject object,
                                                                  final ValueType valueType,
                                                                  final BiFunction<JsonObject, String, T> function,
                                                                  final String... names) {
        checkArguments(object, names);
        if (names.length == 1) {
            if (!object.containsKey(names[0])) {
                return Optional.empty();
            }
            ValueType actualValueType = object.get(names[0]).getValueType();

            if (ValueType.NULL.equals(actualValueType)) {
                return Optional.empty();
            }

            if (!valueType.equals(actualValueType)) {
                throw new IllegalStateException(String.format(FIELD_IS_NOT_A_TYPE, names[0], valueType.toString()));
            }

            return Optional.of(function.apply(object, names[0]));
        } else {
            return getJsonObject(object, names[0])
                    .flatMap(subObject -> getJsonValue(subObject, valueType, function, Arrays.copyOfRange(names, 1, names.length)));
        }
    }

    /**
     * Returns the (possibly nested) string value at the given field name path as a plain
     * {@link String}, if it exists and is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param names  one or more field names forming a path into nested objects
     * @return an {@link Optional} containing the string value, or {@link Optional#empty()} if
     *         the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON string
     */
    public static Optional<String> getString(final JsonObject object, final String... names) {
        return getJsonValue(object, ValueType.STRING, JsonObject::getJsonString, names)
                .map(JsonString::getString);
    }

    /**
     * Returns the (possibly nested) string value at the given field name path parsed as a
     * {@link UUID}, if it exists and is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param names  one or more field names forming a path into nested objects
     * @return an {@link Optional} containing the {@link UUID}, or {@link Optional#empty()} if
     *         the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but is not a JSON string, or if the
     *                                  string value cannot be parsed as a UUID
     */
    public static Optional<UUID> getUUID(final JsonObject object, final String... names) {
        return getString(object, names)
                .map(string -> {
                    try {
                        return UUID.fromString(string);
                    } catch (IllegalArgumentException ex) {
                        throw new IllegalStateException(String.format("Retrieved string '%s' is not a UUID", string), ex);
                    }
                });
    }

    /**
     * Returns the (possibly nested) number value at the given field name path as a {@link Long},
     * if it exists and is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param names  one or more field names forming a path into nested objects
     * @return an {@link Optional} containing the long value, or {@link Optional#empty()} if the
     *         field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON number
     */
    public static Optional<Long> getLong(final JsonObject object, final String... names) {
        return getJsonValue(object, ValueType.NUMBER, JsonObject::getJsonNumber, names)
                .map(JsonNumber::longValue);
    }

    /**
     * Returns the boolean value mapped to the given field name, if it exists.
     *
     * <p>Unlike the other getter methods this one does not support nested paths; it operates
     * only on a single field name.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param name   the field name whose associated value is to be returned
     * @return an {@link Optional} containing the boolean value, or {@link Optional#empty()} if
     *         the field is absent
     * @throws IllegalStateException if the field exists but its value is not a JSON boolean
     */
    public static Optional<Boolean> getBoolean(final JsonObject object, final String name) {
        try {
            return object.containsKey(name) ? Optional.of(object.getBoolean(name)) : Optional.empty();
        } catch (ClassCastException e) {
            throw new IllegalStateException(String.format(FIELD_IS_NOT_A_TYPE, name, "Boolean"));
        }
    }

    /**
     * Returns the (possibly nested) JSON array at the given field name path as a typed
     * {@link List}, if it exists and is not JSON null.
     *
     * @param object the {@link JsonObject} from which to retrieve the value
     * @param clazz  the {@link JsonValue} subtype that each array element must be cast to
     * @param names  one or more field names forming a path into nested objects
     * @param <R>    the {@link JsonValue} subtype of the list elements
     * @return an {@link Optional} containing an immutable list of values, or
     *         {@link Optional#empty()} if the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON array
     */
    public static <R extends JsonValue> Optional<List<R>> getList(final JsonObject object, final Class<R> clazz, final String... names) {
        return getJsonValue(object, ValueType.ARRAY, JsonObject::getJsonArray, names)
                .map(jsonArray -> jsonArray.getValuesAs(clazz))
                .map(ImmutableList::copyOf);
    }

    /**
     * Returns the (possibly nested) JSON array at the given field name path as a {@link List} of
     * converted values, if it exists and is not JSON null.
     *
     * <p>Each element in the JSON array is first cast to {@code jsonClazz} and then passed
     * through {@code converter} to produce the final list element type.
     *
     * @param object    the {@link JsonObject} from which to retrieve the value
     * @param jsonClazz the {@link JsonValue} subtype that each array element is stored as
     * @param converter a function that converts each {@link JsonValue} element to type {@code R}
     * @param names     one or more field names forming a path into nested objects
     * @param <R>       the type of items in the returned list
     * @param <J>       the {@link JsonValue} subtype of the array elements
     * @return an {@link Optional} containing an immutable list of converted values, or
     *         {@link Optional#empty()} if the field is absent or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON array
     */
    public static <R, J extends JsonValue> Optional<List<R>> getList(final JsonObject object,
                                                                     final Class<J> jsonClazz,
                                                                     final Function<J, R> converter,
                                                                     final String... names) {
        return getList(object, jsonClazz, names)
                .map(list -> list.stream()
                        .map(converter)
                        .collect(Collectors.toList()))
                .map(ImmutableList::copyOf);
    }

    /**
     * Returns the (possibly nested) JSON array at the given field name path as a {@link List} of
     * {@link UUID} values. Each array element must be a JSON string containing a valid UUID.
     *
     * @param object the {@link JsonObject} from which to retrieve the list
     * @param names  one or more field names forming a path into nested objects
     * @return an immutable list of {@link UUID} values, or an empty list if the field is absent
     *         or its value is JSON null
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  the first name is null or empty
     * @throws IllegalStateException    if the field exists but its value is not a JSON array
     */
    public static List<UUID> getUUIDs(final JsonObject object, final String... names) {
        return getList(object, JsonString.class, jsonString -> UUID.fromString(jsonString.getString()), names)
                .orElse(Collections.emptyList());
    }

    /**
     * Creates a {@link JsonObjectBuilder} initialised with all fields from {@code source} for
     * which {@code filter} returns {@code true}.
     *
     * @param source the {@link JsonObject} whose fields are to be selectively copied
     * @param filter a predicate applied to each field name; only fields for which it returns
     *               {@code true} are included in the builder
     * @return a {@link JsonObjectBuilder} containing the filtered fields
     */
    public static JsonObjectBuilder createObjectBuilderWithFilter(final JsonObject source, Function<String, Boolean> filter) {
        JsonObjectBuilder builder = getJsonBuilderFactory().createObjectBuilder();
        source.entrySet().stream().filter(e -> filter.apply(e.getKey())).forEach(x -> builder.add(x.getKey(), x.getValue()));
        return builder;
    }

    /**
     * Creates a {@link JsonObjectBuilder} initialised with all fields from {@code source}.
     *
     * @param source the {@link JsonObject} whose fields are to be copied
     * @return a {@link JsonObjectBuilder} containing all fields from {@code source}
     */
    public static JsonObjectBuilder createObjectBuilder(final JsonObject source) {
        return createObjectBuilderWithFilter(source, x -> true);
    }

    /**
     * Validates that the arguments passed to a getter method are well-formed.
     *
     * @param object the {@link JsonObject} being queried; must not be null
     * @param names  the field name path; must contain at least one non-null, non-empty name
     * @throws IllegalArgumentException if {@code object} is null, {@code names} is empty, or
     *                                  {@code names[0]} is null or empty
     */
    private static void checkArguments(final JsonObject object, final String... names) {
        if (object == null) {
            throw new IllegalArgumentException("Json object cannot be null");
        }
        if (names.length == 0) {
            throw new IllegalArgumentException("At least one level of field name must be provided");
        }
        if (names[0] == null || names[0].isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be null or empty");
        }
    }

    /**
     * Converts a collection of values into a {@link JsonArray} by applying {@code converter} to
     * each element.
     *
     * @param entries   the collection of values to convert
     * @param converter a function that maps each entry to a {@link JsonValue}
     * @param <T>       the type of element in the collection
     * @return a {@link JsonArray} containing the converted entries in iteration order
     */
    public static <T> JsonArray toJsonArray(final Collection<T> entries, final Function<T, JsonValue> converter) {
        final JsonArrayBuilder arrayBuilder = getJsonBuilderFactory().createArrayBuilder();
        entries.forEach(entry -> arrayBuilder.add(converter.apply(entry)));
        return arrayBuilder.build();
    }
}
