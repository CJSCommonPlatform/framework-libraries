package uk.gov.justice.schema.service;

import uk.gov.justice.schema.catalog.CatalogLoader;
import uk.gov.justice.schema.catalog.CatalogToSchemaResolver;
import uk.gov.justice.schema.catalog.ClasspathCatalogLoader;
import uk.gov.justice.schema.catalog.FileContentsAsStringLoader;
import uk.gov.justice.schema.catalog.JsonSchemaLoader;
import uk.gov.justice.schema.catalog.JsonStringToSchemaConverter;
import uk.gov.justice.schema.catalog.SchemaResolver;
import uk.gov.justice.schema.catalog.SchemaResolverAndLoader;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.schema.client.SchemaClientFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CatalogLoaderProducer {

    @Inject
    private ObjectMapper objectMapper;

    @Produces
    public CatalogLoader createCatalogLoader() {
        final FileContentsAsStringLoader fileContentsAsStringLoader = new FileContentsAsStringLoader();
        final JsonSchemaLoader jsonSchemaLoader = new JsonSchemaLoader(fileContentsAsStringLoader);

        final JsonStringToSchemaConverter jsonStringToSchemaConverter = new JsonStringToSchemaConverter();
        final SchemaResolverAndLoader schemaResolverAndLoader = new SchemaResolverAndLoader(jsonStringToSchemaConverter);

        final ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();

        final SchemaClientFactory schemaClientFactory = new SchemaClientFactory();

        final UrlConverter urlConverter = new UrlConverter();
        final ClasspathCatalogLoader classpathCatalogLoader = new ClasspathCatalogLoader(objectMapper, classpathResourceLoader, urlConverter);

        final SchemaResolver schemaResolver = new SchemaResolver(urlConverter);
        final CatalogToSchemaResolver catalogToSchemaResolver = new CatalogToSchemaResolver(classpathCatalogLoader, schemaResolver);

        return new CatalogLoader(
                schemaResolverAndLoader,
                catalogToSchemaResolver,
                jsonSchemaLoader,
                schemaClientFactory);
    }
}
