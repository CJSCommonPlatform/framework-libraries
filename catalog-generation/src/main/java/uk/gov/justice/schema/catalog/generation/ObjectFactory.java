package uk.gov.justice.schema.catalog.generation;

import uk.gov.justice.schema.catalog.util.UrlConverter;
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
}
