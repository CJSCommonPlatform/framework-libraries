package uk.gov.justice.schema.catalog.generation;

import uk.gov.justice.schema.catalog.CatalogObjectFactory;

public class GenerationObjectFactory extends CatalogObjectFactory {

    public CatalogGenerationContext catalogGenerationContext() {
        return new CatalogGenerationContext();
    }

    public CatalogWriter catalogWriter() {
        return new CatalogWriter(objectMapper(), catalogGenerationContext());
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
