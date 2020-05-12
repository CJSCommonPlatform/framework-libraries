package uk.gov.justice.schema.catalog;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import java.nio.file.Path;
import java.util.Collection;

import org.everit.json.schema.Schema;
import org.json.JSONObject;

public class SchemaCatalogResolver {

    private final RawCatalog rawCatalog;
    private final SchemaClientFactory schemaClientFactory;
    private final JsonToSchemaConverter jsonStringToSchemaConverter;

    public SchemaCatalogResolver(final RawCatalog rawCatalog,
                                 final SchemaClientFactory schemaClientFactory,
                                 final JsonToSchemaConverter jsonStringToSchemaConverter) {
        this.rawCatalog = rawCatalog;
        this.schemaClientFactory = schemaClientFactory;
        this.jsonStringToSchemaConverter = jsonStringToSchemaConverter;
    }

    public Schema loadSchema(final JSONObject jsonSchema) {
        return jsonStringToSchemaConverter.convert(
                jsonSchema,
                schemaClientFactory.create(rawCatalog));
    }

    /**
     * Updates the raw catalogue cache with raw json schemas that are not on the classpath
     *
     * @param basePath the base directory to load the resources from
     * @param paths    the resources to parse
     */
    public void updateCatalogSchemaCache(final Path basePath, final Collection<Path> paths) {
        this.rawCatalog.updateCatalogSchemaCache(basePath, paths);
    }
}
