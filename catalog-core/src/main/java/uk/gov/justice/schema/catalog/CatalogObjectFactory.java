package uk.gov.justice.schema.catalog;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A simple cheap way of avoiding having to use a dependency injection framework in the project.
 * Gets a instance of an object with the object's dependencies instantiated and injected.
 */
public class CatalogObjectFactory {

    /**
     * @return a new instance of {@link UrlConverter}
     */
    public UrlConverter urlConverter() {
        return new UrlConverter();
    }

    /**
     * @return a new instance of {@link ObjectMapper}
     */
    public ObjectMapper objectMapper() {
        return new ObjectMapperProducer().objectMapper();
    }

    /**
     * @return a new instance of {@link UriResolver}
     */
    public UriResolver uriResolver() {
        return new UriResolver();
    }

    /**
     * @return a new instance of {@link JsonStringToSchemaConverter}
     */
    public JsonStringToSchemaConverter jsonStringToSchemaConverter() {
        return new JsonStringToSchemaConverter();
    }

    /**
     * @return a new instance of {@link ClasspathResourceLoader}
     */
    public ClasspathResourceLoader classpathResourceLoader() {
        return new ClasspathResourceLoader();
    }

    /**
     * @return a new instance of {@link ClasspathCatalogLoader}
     */
    public ClasspathCatalogLoader classpathCatalogLoader() {
        return new ClasspathCatalogLoader(
                objectMapper(),
                classpathResourceLoader(),
                urlConverter());
    }

    /**
     * @return a new instance of {@link SchemaResolver}
     */
    public SchemaResolver schemaResolver() {
        return new SchemaResolver(urlConverter(), uriResolver());
    }

    /**
     * @return a new instance of {@link CatalogToSchemaResolver}
     */
    public CatalogToSchemaResolver catalogToSchemaResolver() {
        return new CatalogToSchemaResolver(
                classpathCatalogLoader(),
                schemaResolver(),
                getLogger(CatalogToSchemaResolver.class));
    }

    /**
     * @return a new instance of {@link FileContentsAsStringLoader}
     */
    public FileContentsAsStringLoader fileContentsAsStringLoader() {
        return new FileContentsAsStringLoader();
    }

    /**
     * @return a new instance of {@link JsonSchemaFileLoader}
     */
    public JsonSchemaFileLoader jsonSchemaFileLoader() {
        return new JsonSchemaFileLoader(fileContentsAsStringLoader(), catalogToSchemaResolver());
    }

    /**
     * @return a new instance of {@link SchemaClientFactory}
     */
    public SchemaClientFactory schemaClientFactory() {
        return new SchemaClientFactory();
    }

    /**
     * @return a new instance of {@link Catalog}
     */
    public Catalog catalog() {
        return new Catalog(
                rawCatalog(),
                schemaClientFactory(),
                jsonStringToSchemaConverter());
    }

    /**
     * @return a new instance of {@link RawCatalog}
     */
    public RawCatalog rawCatalog() {
        final RawCatalog rawCatalog = new RawCatalog(jsonSchemaFileLoader());
        rawCatalog.initialize();

        return rawCatalog;
    }
}

