package uk.gov.justice.schema.catalog.generation.effective;

import uk.gov.justice.schema.catalog.CatalogObjectFactory;

/**
 * Factory for creating instances of the effective JSON schema generation components.
 * Avoids the need for a dependency injection framework in library/plugin code.
 */
public class EffectiveJsonSchemaObjectFactory {

    public DefinitionNameFactory definitionNameFactory() {
        return new DefinitionNameFactory();
    }

    public JsonSchemaInliner jsonSchemaInliner() {
        return new JsonSchemaInliner(definitionNameFactory());
    }

    public EffectiveJsonSchemaWriter effectiveJsonSchemaWriter() {
        return new EffectiveJsonSchemaWriter();
    }

    public EffectiveJsonSchemaGenerator effectiveJsonSchemaGenerator() {
        return new EffectiveJsonSchemaGenerator(jsonSchemaInliner(), effectiveJsonSchemaWriter());
    }

    public CatalogJsonSchemaLoader catalogJsonSchemaLoader() {
        return new CatalogJsonSchemaLoader(new CatalogObjectFactory().objectMapper());
    }
}
