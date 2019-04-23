
package uk.gov.justice.json.jolt;

import static javax.json.Json.createReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.json.JsonObject;
import javax.json.JsonReader;

public class JsonHelper {
    public static JsonObject readJson(final String filePath) {
        try (final InputStream inputStream = JsonHelper.class.getResourceAsStream(filePath);
             final JsonReader jsonReader = createReader(inputStream)) {
            return jsonReader.readObject();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }



    public static String readJsonAsString(String filePath) {
        try (final InputStream inputStream = JsonHelper.class.getResourceAsStream(filePath);
             final JsonReader jsonReader = createReader(inputStream)) {
            return jsonReader.readObject().toString();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}