package uk.gov.justice.schema.catalog;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CatalogObjectFactory {

    public UrlConverter urlConverter() {
        return new UrlConverter();
    }

    public ObjectMapper objectMapper() {
        return new ObjectMapperProducer().objectMapper();
    }

    public UriResolver uriResolver() {
        return new UriResolver();
    }

    public JsonStringToSchemaConverter jsonStringToSchemaConverter() {
        return new JsonStringToSchemaConverter();
    }

    public ClasspathResourceLoader classpathResourceLoader() {
        return new ClasspathResourceLoader();
    }

    public ClasspathCatalogLoader classpathCatalogLoader() {
        return new ClasspathCatalogLoader(
                objectMapper(),
                classpathResourceLoader(),
                urlConverter());
    }

    public SchemaResolver schemaResolver() {
        return new SchemaResolver(urlConverter(), uriResolver());
    }

    public CatalogToSchemaResolver catalogToSchemaResolver() {
        return new CatalogToSchemaResolver(
                classpathCatalogLoader(),
                schemaResolver(),
                getLogger(CatalogToSchemaResolver.class));
    }

    public FileContentsAsStringLoader fileContentsAsStringLoader() {
        return new FileContentsAsStringLoader();
    }

    public JsonSchemaFileLoader jsonSchemaFileLoader() {
        return new JsonSchemaFileLoader(fileContentsAsStringLoader(), catalogToSchemaResolver());
    }

    public SchemaClientFactory schemaClientFactory() {
        return new SchemaClientFactory();
    }

    public Catalog catalog() {
        return new Catalog(
                rawCatalog(),
                schemaClientFactory(),
                jsonStringToSchemaConverter());
    }

    public RawCatalog rawCatalog() {
        final RawCatalog rawCatalog = new RawCatalog(jsonSchemaFileLoader());
        rawCatalog.initialize();

        return rawCatalog;
    }
}

