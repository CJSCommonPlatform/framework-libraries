package uk.gov.justice.schema.catalog;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.json.Json.createReader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class CatalogUpdater {

    private final Logger logger;

    public CatalogUpdater(final Logger logger) {
        this.logger = logger;
    }

    public void updtateRawCatalog(Map<String, String> schemaIdsToRawJsonSchemaCache, Collection<Path> paths) {

        paths.forEach(path ->{

            final String schema;
            try {
                schema = IOUtils.toString(path.toUri().toURL(), UTF_8);
                try (final JsonReader reader = createReader(new StringReader(schema))) {
                    final JsonObject jsonObject = reader.readObject();

                    if (jsonObject.containsKey("id")) {
                        final String id = jsonObject.getString("id");
                        schemaIdsToRawJsonSchemaCache.put(id, schema);
                    }
                }
                //logger.warn(schema.format());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });
    }
}
