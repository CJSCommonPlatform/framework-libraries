package uk.gov.justice.schema.catalog;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static javax.json.Json.createReader;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonObject;
import javax.json.JsonReader;

import javafx.util.Pair;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Main cache of all Json Schemas found on the classpath mapped by their Schema id.
 * All Schemas are the raw unresolved contents of the Json Schema file as a {@link String}
 */
public class RawCatalog {

    private final JsonSchemaFileLoader jsonSchemaFileLoader;


    private final CatalogUpdater catalogUpdater;

    private Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();

    public RawCatalog(final JsonSchemaFileLoader jsonSchemaFileLoader,
                      final CatalogUpdater catalogUpdater) {
        this.jsonSchemaFileLoader = jsonSchemaFileLoader;
        this.catalogUpdater = catalogUpdater;
    }



    public void updtateCatalog(Collection<Path> paths) {
        catalogUpdater.updtateRawCatalog(schemaIdsToRawJsonSchemaCache, paths);

    }

    /**
     * Initializes the cache of raw json schemas by scanning the classpath and
     * loading all json schemas it finds.
     */
    public void initialize() {
        schemaIdsToRawJsonSchemaCache = jsonSchemaFileLoader.loadSchemas();
    }

    /**
     * Gets the raw unresolved json of a Schema file found on the classpath by its id.
     * @param schemaId The id of the required json schema file
     * @return An {@link Optional} containing the raw json schema of the schema file with
     * the specified id, if it exists.
     */
    public Optional<String> getRawJsonSchema(final String schemaId) {

        //updateCatalogSchemaCache(schemaId);
        return ofNullable(schemaIdsToRawJsonSchemaCache.get(schemaId));
    }

/*    private void updateCatalogSchemaCache(final String schemaId) {
        if(schemaIdsToRawJsonSchemaCache.get(schemaId) == null){
            try {
                final Optional<Pair<String, String>> schemaDetails = schemaIdAndUriParser.parse(new URL(schemaId));
                if (schemaDetails.isPresent()) {
                    schemaIdsToRawJsonSchemaCache.put(schemaDetails.get().getKey(), schemaDetails.get().getValue());
                }
            } catch (MalformedURLException e) {
                logger.error(e.getMessage());
            }
        }
    }*/
}
