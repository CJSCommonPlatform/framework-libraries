package uk.gov.justice.schema.catalog;

import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Main cache of all Json Schemas found on the classpath mapped by their Schema id.
 * All Schemas are the raw unresolved contents of the Json Schema file as a {@link String}
 */
public class RawCatalog {

    private final JsonSchemaFileLoader jsonSchemaFileLoader;

    private Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();

    public RawCatalog(final JsonSchemaFileLoader jsonSchemaFileLoader) {
        this.jsonSchemaFileLoader = jsonSchemaFileLoader;
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
        return ofNullable(schemaIdsToRawJsonSchemaCache.get(schemaId));
    }
}
