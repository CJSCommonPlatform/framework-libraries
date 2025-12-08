
package uk.gov.justice.json.jolt;


import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.json.JsonObject;
import javax.json.JsonReader;

import static uk.gov.justice.services.messaging.JsonObjects.getJsonReaderFactory;

public class JsonHelper {
    public static JsonObject readJson(final String filePath) {
        try (final InputStream inputStream = JsonHelper.class.getResourceAsStream(filePath);
             final JsonReader jsonReader = getJsonReaderFactory().createReader(inputStream)) {
            return jsonReader.readObject();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}