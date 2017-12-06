package uk.gov.justice.schema.catalog;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import java.util.Optional;

import org.everit.json.schema.Schema;

/**
 * Main entry point into the application. Gets a fully resolved Schema by the Schema's id.
 *
 * <p>
 *     Usage:
 * </p>
 *
 *     <pre>
 *     {@code
 *
 *       final String json = "{\"some\": \"json\"}";
 *
 *       final String schemaId = "http://justice.gov.uk/domain/schemas/some-json-schema.json";
 *
 *       final Catalog catalog = new CatalogObjectFactory().catalog();
 *       final Optional<Schema> schema = catalog.getSchema(schemaId);
 *
 *       if(schema.isPresent()) {
 *           schema.get().validate(json);
 *       }
 *     }
 *     </pre>
 */
public class Catalog {

    private final RawCatalog rawCatalog;
    private final SchemaClientFactory schemaClientFactory;
    private final JsonStringToSchemaConverter jsonStringToSchemaConverter;

    public Catalog(
            final RawCatalog rawCatalog,
            final SchemaClientFactory schemaClientFactory,
            final JsonStringToSchemaConverter jsonStringToSchemaConverter) {
        this.rawCatalog = rawCatalog;
        this.schemaClientFactory = schemaClientFactory;
        this.jsonStringToSchemaConverter = jsonStringToSchemaConverter;
    }

    /**
     * Gets a a fully resolved json {@link Schema} by its schema id
     *
     * @param schemaId The id of the required schema
     * @return a fully resolved {@link Schema}
     */
    public Optional<Schema> getSchema(final String schemaId) {

        final Optional<String> rawJsonSchema = rawCatalog.getRawJsonSchema(schemaId);

        if (rawJsonSchema.isPresent()) {
            final Schema schema = jsonStringToSchemaConverter.convert(
                    rawJsonSchema.get(),
                    schemaClientFactory.create(rawCatalog));

            return of(schema);
        }

        return empty();
    }
}
