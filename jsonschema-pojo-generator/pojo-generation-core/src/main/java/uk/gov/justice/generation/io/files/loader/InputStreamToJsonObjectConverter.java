package uk.gov.justice.generation.io.files.loader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 * Utility to convert an {@link InputStream} to a {@link JSONObject}
 */
public class InputStreamToJsonObjectConverter {

    /**
     * Converts an {@link InputStream} to a {@link JSONObject}
     *
     * @param inputStream - the InputStream to convert
     * @return converted JSONObject
     * @throws IOException if conversion fails
     */
    public JSONObject toJsonObject(final InputStream inputStream) throws IOException {
        return new JSONObject(IOUtils.toString(inputStream));
    }
}
