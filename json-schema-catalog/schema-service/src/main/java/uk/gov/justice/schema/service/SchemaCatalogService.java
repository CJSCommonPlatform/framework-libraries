package uk.gov.justice.schema.service;

import uk.gov.justice.schema.catalog.Catalog;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.everit.json.schema.Schema;

/**
 * A service that finds a fully resolved json schema by its schema id. Allows a schema catalog
 * to be used in an application that uses a dependency injection framework. All schemas are
 * looked up on the fly, then cached.
 */
@ApplicationScoped
public class SchemaCatalogService {

    private final Map<String, Optional<Schema>> schemaMap = new ConcurrentHashMap<>();

    @Inject
    private Catalog catalog;

    /**
     * Finds a json schema file on the classpath by its schema id
     * @param schemaId The id of the schema
     * @return An {@link Optional} containing the fully resolved schema or empty if not found
     */
    public Optional<Schema> findSchema(final String schemaId) {

        if(schemaMap.containsKey(schemaId)) {
            return schemaMap.get(schemaId);
        }

        final Optional<Schema> schema = catalog.getSchema(schemaId);

        schemaMap.put(schemaId, schema);

        return schema;
    }
}
