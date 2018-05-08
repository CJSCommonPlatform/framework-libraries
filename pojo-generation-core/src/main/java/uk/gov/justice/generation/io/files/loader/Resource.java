package uk.gov.justice.generation.io.files.loader;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Path;

import org.json.JSONObject;

public class Resource {

    private final Path basePath;
    private final Path resourcePath;
    private final ResourceLoader resourceLoader;
    private final InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter;

    public Resource(final Path basePath,
                    final Path resourcePath,
                    final ResourceLoader resourceLoader,
                    final InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter) {
        this.basePath = basePath;
        this.resourcePath = resourcePath;
        this.resourceLoader = resourceLoader;
        this.inputStreamToJsonObjectConverter = inputStreamToJsonObjectConverter;
    }

    public JSONObject asJsonObject() {
        try {
            return inputStreamToJsonObjectConverter
                    .toJsonObject(resourceLoader.loadFrom(basePath, resourcePath));
        } catch (IOException ex) {
            throw new ResourceLoadingException(format("Unable to load resource: %s", resourcePath), ex);
        }
    }
}
