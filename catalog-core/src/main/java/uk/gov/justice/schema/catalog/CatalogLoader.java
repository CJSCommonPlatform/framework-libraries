package uk.gov.justice.schema.catalog;

import uk.gov.justice.schema.client.SchemaClientFactory;

import java.util.Map;

import javax.inject.Inject;

import org.everit.json.schema.Schema;

public class CatalogLoader {

    private final SchemaResolverAndLoader schemaResolverAndLoader;
    private final CatalogToSchemaResolver catalogToSchemaResolver;
    private final JsonSchemaFileLoader jsonSchemaFileLoader;
    private final SchemaClientFactory schemaClientFactory;

    @Inject
    public CatalogLoader(
            final SchemaResolverAndLoader schemaResolverAndLoader,
            final CatalogToSchemaResolver catalogToSchemaResolver,
            final JsonSchemaFileLoader jsonSchemaFileLoader,
            final SchemaClientFactory schemaClientFactory) {
        this.schemaResolverAndLoader = schemaResolverAndLoader;
        this.catalogToSchemaResolver = catalogToSchemaResolver;
        this.jsonSchemaFileLoader = jsonSchemaFileLoader;
        this.schemaClientFactory = schemaClientFactory;
    }

    public Map<String, Schema> loadCatalogsFromClasspath() {

        final Map<String, String> urlsToJson = jsonSchemaFileLoader.loadJsonFrom(
                catalogToSchemaResolver.resolveSchemaLocations()
        );

        return schemaResolverAndLoader.loadSchemas(
                urlsToJson,
                schemaClientFactory.create(urlsToJson));
    }
}
