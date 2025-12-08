package uk.gov.justice.schema.catalog;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonReaderFactory;

import uk.gov.justice.schema.catalog.exception.InvalidJsonFileException;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Update a {@link RawCatalog} cache, to add resource paths that are not on the classpath.
 */
public class CatalogUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogUpdater.class);

    private static final String CLASSPATH = "CLASSPATH";

    /**
     * Updates the cache with raw json schemas that are not on the classpath
     *
     * @param basePath the base directory to load the resources from
     * @param paths    the resources to parse
     */
    public void updateRawCatalog(final Map<String, String> schemaIdsToRawJsonSchemaCache, final Path basePath, final Collection<Path> paths) {

        paths.forEach(path -> {
            final Path updatedPath = Paths.get(format("%s/%s", basePath.toString(), path.toString()));

            try {
                if (!updatedPath.toString().contains(CLASSPATH)) {
                    final String schema = IOUtils.toString(updatedPath.toUri().toURL(), UTF_8);
                    try (final JsonReader reader = getJsonReaderFactory().createReader(new StringReader(schema))) {
                        try {
                            final JsonObject jsonObject = reader.readObject();

                            if (jsonObject.containsKey("id")) {
                                final String id = jsonObject.getString("id");
                                schemaIdsToRawJsonSchemaCache.put(id, schema);
                            }
                        } catch (final JsonParsingException e) {
                            final String errorMessage = format("Unable to parse Json file: %s", updatedPath);
                            LOGGER.error(errorMessage);
                            throw new InvalidJsonFileException(errorMessage, e);
                        }
                    }
                }

                LOGGER.warn(format("Failed to generate catalog. Schema '%s' has no id", path.toUri().toURL()));

            } catch (IOException e) {
                LOGGER.warn("Unable to read schema file", e);
            }
        });
    }
}
