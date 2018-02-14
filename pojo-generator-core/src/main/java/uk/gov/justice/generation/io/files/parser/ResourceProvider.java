package uk.gov.justice.generation.io.files.parser;

import uk.gov.justice.generation.io.files.loader.InputStreamToJsonObjectConverter;
import uk.gov.justice.generation.io.files.loader.Resource;
import uk.gov.justice.generation.io.files.loader.ResourceLoader;

import java.nio.file.Path;

public class ResourceProvider {

    private InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter;

    public ResourceProvider(final InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter) {
        this.inputStreamToJsonObjectConverter = inputStreamToJsonObjectConverter;
    }

    public Resource getResource(final Path basePath, final Path resourcePath, final ResourceLoader resourceLoader) {
        return new Resource(basePath, resourcePath, resourceLoader, inputStreamToJsonObjectConverter);
    }
}
