package uk.gov.justice.services.common.converter;

import java.io.StringReader;

import javax.json.JsonObject;
import javax.json.JsonReader;

import static uk.gov.justice.services.messaging.JsonObjects.getJsonReaderFactory;

/**
 * Converts a Json String to a JsonObject.
 */
public class StringToJsonObjectConverter implements Converter<String, JsonObject> {

    @Override
    public JsonObject convert(final String source) {
        try (final JsonReader reader = getJsonReaderFactory().createReader(new StringReader(source))) {
            return reader.readObject();
        }
    }

}
