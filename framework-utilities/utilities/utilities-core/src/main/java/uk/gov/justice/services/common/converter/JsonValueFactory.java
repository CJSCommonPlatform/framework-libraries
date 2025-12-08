package uk.gov.justice.services.common.converter;

import static javax.json.JsonValue.FALSE;
import static javax.json.JsonValue.TRUE;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

import java.math.BigDecimal;

import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * Factory for producing JsonValue based values for literals.
 */
public class JsonValueFactory {

    private static final String PROPERTY_NAME = "PROPERTY_NAME";

    public JsonValue build(final Object value) {
        if (value instanceof String) {
            return buildJsonString((String) value);
        } else if (value instanceof Integer) {
            return buildJsonNumber((Integer) value);
        } else if (value instanceof Long) {
            return buildJsonNumber((Long) value);
        } else if (value instanceof BigDecimal) {
            return buildJsonNumber((BigDecimal) value);
        } else if (value instanceof Boolean) {
            return buildJsonBoolean((Boolean) value);
        } else {
            return buildJsonString(value.toString());
        }
    }

    /**
     * Construct a JsonString from a String. The javax.json API does not have an interface for
     * doing this nicely, so we have to bodge it.
     *
     * @param string the value
     * @return a JsonString with the given value
     */
    private static JsonString buildJsonString(final String string) {
        return getJsonBuilderFactory().createObjectBuilder().add(PROPERTY_NAME, string).build().getJsonString(PROPERTY_NAME);
    }

    /**
     * Construct a JsonNumber from an Integer. The javax.json API does not have an interface for
     * doing this nicely, so we have to bodge it.
     *
     * @param integer the value
     * @return a JsonNumber with the given value
     */
    private static JsonNumber buildJsonNumber(final Integer integer) {
        return getJsonBuilderFactory().createObjectBuilder().add(PROPERTY_NAME, integer).build().getJsonNumber(PROPERTY_NAME);
    }

    /**
     * Construct a JsonNumber from an Long. The javax.json API does not have an interface for
     * doing this nicely, so we have to bodge it.
     *
     * @param longValue the value
     * @return a JsonNumber with the given value
     */
    private static JsonNumber buildJsonNumber(final Long longValue) {
        return getJsonBuilderFactory().createObjectBuilder().add(PROPERTY_NAME, longValue).build().getJsonNumber(PROPERTY_NAME);
    }

    /**
     * Construct a JsonNumber from a BigDecimal. The javax.json API does not have an interface for
     * doing this nicely, so we have to bodge it.
     *
     * @param bigDecimal the value
     * @return a JsonNumber with the given value
     */
    private static JsonNumber buildJsonNumber(final BigDecimal bigDecimal) {
        return getJsonBuilderFactory().createObjectBuilder().add(PROPERTY_NAME, bigDecimal).build().getJsonNumber(PROPERTY_NAME);
    }

    /**
     * Construct a JsonValue from a Boolean. The javax.json API does not have an interface for
     * doing this nicely, so we have to bodge it.
     *
     * @param bool the value
     * @return a JsonValue with the given value
     */
    private static JsonValue buildJsonBoolean(final Boolean bool) {
        return bool ? TRUE : FALSE;
    }
}

