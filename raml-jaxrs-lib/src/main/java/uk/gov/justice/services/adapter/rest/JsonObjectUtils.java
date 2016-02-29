package uk.gov.justice.services.adapter.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Created by david on 15/02/16.
 */
public final class JsonObjectUtils {
    
    private JsonObjectUtils() {
        
    }

    public static JsonObjectBuilder createObject(final JsonObject source) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (String key : source.keySet()) {
            builder = builder.add(key, source.get(key));
        }
        return builder;
    }
}
