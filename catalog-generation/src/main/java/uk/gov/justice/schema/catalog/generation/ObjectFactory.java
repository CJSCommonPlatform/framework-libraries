package uk.gov.justice.schema.catalog.generation;

import uk.gov.justice.schema.catalog.CatalogLoader;
import uk.gov.justice.schema.catalog.CatalogToSchemaResolver;
import uk.gov.justice.schema.catalog.ClasspathCatalogLoader;
import uk.gov.justice.schema.catalog.FileContentsAsStringLoader;
import uk.gov.justice.schema.catalog.JsonSchemaFileLoader;
import uk.gov.justice.schema.catalog.JsonStringToSchemaConverter;
import uk.gov.justice.schema.catalog.SchemaResolver;
import uk.gov.justice.schema.catalog.SchemaResolverAndLoader;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.schema.client.SchemaClientFactory;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectFactory {

    public CatalogGenerationContext catalogGenerationConstants() {
        return new CatalogGenerationContext();
    }

    public UrlConverter urlConverter() {
        return new UrlConverter();
    }

    public ObjectMapper objectMapper() {
        return new ObjectMapperProducer().objectMapper();
    }

    public CatalogWriter catalogWriter() {
        return new CatalogWriter(objectMapper(), catalogGenerationConstants());
    }

    public SchemaIdParser schemaIdParser() {
        return new SchemaIdParser(urlConverter());
    }

    public CatalogObjectGenerator catalogObjectGenerator() {
        return new CatalogObjectGenerator(schemaDefParser());
    }

    public SchemaDefParser schemaDefParser() {
        return new SchemaDefParser(schemaIdParser());
    }

    public CatalogGenerationRunner catalogGenerationRunner() {
        return new CatalogGenerationRunner(
                catalogObjectGenerator(),
                catalogWriter(),
                urlConverter());
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

    public SchemaResolverAndLoader schemaResolverAndLoader() {
        return new SchemaResolverAndLoader(jsonStringToSchemaConverter());
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
       return new CatalogToSchemaResolver(classpathCatalogLoader(), schemaResolver());
    }

    public FileContentsAsStringLoader fileContentsAsStringLoader() {
        return new FileContentsAsStringLoader();
    }

    public JsonSchemaFileLoader jsonSchemaFileLoader() {
        return new JsonSchemaFileLoader(fileContentsAsStringLoader());
    }

    public SchemaClientFactory schemaClientFactory() {
        return new SchemaClientFactory();
    }

    public CatalogLoader catalogLoader() {
        return new CatalogLoader(
                schemaResolverAndLoader(),
                catalogToSchemaResolver(),
                jsonSchemaFileLoader(),
                schemaClientFactory());
    }
}
