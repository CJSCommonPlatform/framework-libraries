package uk.gov.justice.schema.catalog;

import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RawCatalog {

    private final JsonSchemaFileLoader jsonSchemaFileLoader;

    private Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();

    public RawCatalog(final JsonSchemaFileLoader jsonSchemaFileLoader) {
        this.jsonSchemaFileLoader = jsonSchemaFileLoader;
    }

    public void initialize() {
        schemaIdsToRawJsonSchemaCache = jsonSchemaFileLoader.loadSchemas();
    }

    public Optional<String> getRawJsonSchema(final String schemaId) {
        return ofNullable(schemaIdsToRawJsonSchemaCache.get(schemaId));
    }
}
