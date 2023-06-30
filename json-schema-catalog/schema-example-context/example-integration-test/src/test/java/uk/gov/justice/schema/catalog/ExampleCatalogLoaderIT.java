package uk.gov.justice.schema.catalog;

import org.junit.jupiter.api.Test;

public class ExampleCatalogLoaderIT {

    private final Catalog catalog = new CatalogObjectFactory().catalog();

    @Test
    public void shouldMapSchemasOnClasspathToTheirIds2() throws Exception {

        final String id_1 = "http://justice.gov.uk/example/standard/ingredient.json";
        final String json_1 = catalog.getSchema(id_1).get().toString();

        final String id_2 = "http://justice.gov.uk/example/cakeshop/example.add-recipe.json";
        final String json_2 = catalog.getSchema(id_2).get().toString();
    }
}
