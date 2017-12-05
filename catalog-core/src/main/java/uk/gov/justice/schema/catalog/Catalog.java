package uk.gov.justice.schema.catalog;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import java.util.Optional;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;

public class Catalog {

    private final RawCatalog rawCatalog;
    private final SchemaClientFactory schemaClientFactory;
    private final JsonStringToSchemaConverter jsonStringToSchemaConverter;

    public Catalog(final RawCatalog rawCatalog, final SchemaClientFactory schemaClientFactory, final JsonStringToSchemaConverter jsonStringToSchemaConverter) {
        this.rawCatalog = rawCatalog;
        this.schemaClientFactory = schemaClientFactory;
        this.jsonStringToSchemaConverter = jsonStringToSchemaConverter;
    }

    public Optional<Schema> getSchema(final String schemaId) {

        final Optional<String> rawJsonSchema = rawCatalog.getRawJsonSchema(schemaId);

        if (rawJsonSchema.isPresent()) {
            final SchemaClient schemaClient = schemaClientFactory.create(rawCatalog);
            final Schema schema = jsonStringToSchemaConverter.convert(rawJsonSchema.get(), schemaClient);
            return of(schema);
        }

        return empty();
    }
}
