package uk.gov.justice.schema.catalog;

import uk.gov.justice.schema.client.SchemaClientFactory;

import java.util.Map;

import org.everit.json.schema.Schema;

public class CatalogLoader {

    private final SchemaResolverAndLoader schemaResolverAndLoader;
    private final CatalogToSchemaResolver catalogToSchemaResolver;
    private final JsonSchemaLoader jsonSchemaLoader;
    private final SchemaClientFactory schemaClientFactory;

    public CatalogLoader(
            final SchemaResolverAndLoader schemaResolverAndLoader,
            final CatalogToSchemaResolver catalogToSchemaResolver,
            final JsonSchemaLoader jsonSchemaLoader,
            final SchemaClientFactory schemaClientFactory) {
        this.schemaResolverAndLoader = schemaResolverAndLoader;
        this.catalogToSchemaResolver = catalogToSchemaResolver;
        this.jsonSchemaLoader = jsonSchemaLoader;
        this.schemaClientFactory = schemaClientFactory;
    }

    public Map<String, Schema> loadCatalogsFromClasspath() {

        final Map<String, String> urlsToJson = jsonSchemaLoader.loadJsonFrom(
                catalogToSchemaResolver.resolveSchemaLocations()
        );

        return schemaResolverAndLoader.loadSchemas(
                urlsToJson,
                schemaClientFactory.create(urlsToJson));
    }
}
