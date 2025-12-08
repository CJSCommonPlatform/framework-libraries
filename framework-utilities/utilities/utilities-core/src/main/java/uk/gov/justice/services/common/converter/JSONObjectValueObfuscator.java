package uk.gov.justice.services.common.converter;

import static javax.json.JsonValue.NULL;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

import java.util.UUID;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;


/**
 * Obfuscates values of {@link JsonObject}. Keeps the json structure
 */
public final class JSONObjectValueObfuscator {

    private static JsonValueFactory jsonValueFactory = new JsonValueFactory();

    private static final String OBFUSCATED_STRING = "xxx";
    private static final String OBFUSCATED_UUID = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
    private static final Integer OBFUSCATED_NUMERIC = 0;

    private static final JsonValue OBFUSCASTED_JSON_BOOLEAN = JsonValue.FALSE;
    private static final JsonString OBFUSCASTED_JSON_STRING = (JsonString) jsonValueFactory.build(OBFUSCATED_STRING);
    private static final JsonString OBFUSCASTED_JSON_UUID = (JsonString) jsonValueFactory.build(OBFUSCATED_UUID);
    private static final JsonNumber OBFUSCASTED_JSON_NUMBER = (JsonNumber) jsonValueFactory.build(OBFUSCATED_NUMERIC);

    private JSONObjectValueObfuscator() {
    }

    /**
     * Obfuscates values in the passed json
     *
     * @param json the JsonValue to obfuscate
     * @return new json with obfuscatedObject values
     */
    public static JsonValue obfuscated(final JsonValue json) {
        switch (json.getValueType()) {
            case ARRAY:
                final JsonArrayBuilder arrayBuilder = getJsonBuilderFactory().createArrayBuilder();
                final JsonArray jsonArray = (JsonArray) json;
                jsonArray.forEach(o -> arrayBuilder.add(obfuscated(o)));
                return arrayBuilder.build();
            case OBJECT:
                final JsonObjectBuilder obfuscatedJsonObjectBuilder = getJsonBuilderFactory().createObjectBuilder();
                for (String key : ((JsonObject) json).keySet()) {
                    obfuscatedJsonObjectBuilder.add(key, obfuscated(((JsonObject) json).get(key)));
                }
                return obfuscatedJsonObjectBuilder.build();
            case STRING:
                if (isUuid(((JsonString) json))) {
                    return OBFUSCASTED_JSON_UUID;
                } else {
                    return OBFUSCASTED_JSON_STRING;
                }
            case NUMBER:
                return OBFUSCASTED_JSON_NUMBER;
            case TRUE:
            case FALSE:
                return OBFUSCASTED_JSON_BOOLEAN;
            case NULL:
                return NULL;
        }
        return NULL;
    }

    private static boolean isUuid(final JsonString jsonString) {
        try {
            UUID.fromString(jsonString.getString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}