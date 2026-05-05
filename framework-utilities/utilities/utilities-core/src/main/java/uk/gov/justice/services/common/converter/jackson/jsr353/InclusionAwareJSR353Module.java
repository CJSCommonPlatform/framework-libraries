package uk.gov.justice.services.common.converter.jackson.jsr353;

import jakarta.json.JsonValue;

import com.fasterxml.jackson.datatype.jsonp.JSONPModule;

/**
 * Customised version of the JSR353Module that excludes null values.
 */
public class InclusionAwareJSR353Module extends JSONPModule {

    private static final long serialVersionUID = -1112006287109681036L;

    public InclusionAwareJSR353Module() {
        addSerializer(JsonValue.class, new InclusionAwareJsonValueSerializer());
    }
}
