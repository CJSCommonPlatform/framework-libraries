package uk.gov.justice.schema.catalog.generation;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.schema.catalog.CatalogObjectFactory;

/**
 * A simple cheap way of avoiding having to use a dependency injection framework in the project.
 * Gets a instance of an object with the object's dependencies instantiated and injected.
 */
public class GenerationObjectFactory extends CatalogObjectFactory {

    /**
     * @return a new instance of {@link CatalogGenerationContext}
     */
    public CatalogGenerationContext catalogGenerationContext() {
        return new CatalogGenerationContext();
    }

    /**
     * @return a new instance of {@link CatalogWriter}
     */
    public CatalogWriter catalogWriter() {
        return new CatalogWriter(objectMapper(), catalogGenerationContext());
    }

    /**
     * @return a new instance of {@link SchemaIdParser}
     */
    public SchemaIdParser schemaIdParser() {
        return new SchemaIdParser(urlConverter(), getLogger(SchemaIdParser.class));
    }

    /**
     * @return a new instance of {@link CatalogObjectGenerator}
     */
    public CatalogObjectGenerator catalogObjectGenerator() {
        return new CatalogObjectGenerator(schemaDefParser());
    }

    /**
     * @return a new instance of {@link SchemaDefParser}
     */
    public SchemaDefParser schemaDefParser() {
        return new SchemaDefParser(schemaIdParser());
    }

    /**
     * @return a new instance of {@link CatalogGenerationRunner}
     */
    public CatalogGenerationRunner catalogGenerationRunner() {
        return new CatalogGenerationRunner(
                catalogObjectGenerator(),
                catalogWriter(),
                urlConverter());
    }
}
