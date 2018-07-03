package uk.gov.justice.schema.catalog;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

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
}
