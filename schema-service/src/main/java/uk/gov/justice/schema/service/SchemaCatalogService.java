package uk.gov.justice.schema.service;

import uk.gov.justice.schema.catalog.Catalog;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.everit.json.schema.Schema;

@ApplicationScoped
public class SchemaCatalogService {

    private final Map<String, Optional<Schema>> schemaMap = new ConcurrentHashMap<>();

    @Inject
    private Catalog catalog;

    public Optional<Schema> findSchema(final String schemaId) {

        if(schemaMap.containsKey(schemaId)) {
            return schemaMap.get(schemaId);
        }

        final Optional<Schema> schema = catalog.getSchema(schemaId);

        schemaMap.put(schemaId, schema);

        return schema;
    }
}
