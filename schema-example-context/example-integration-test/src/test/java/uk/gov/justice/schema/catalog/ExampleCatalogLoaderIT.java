package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.generation.ObjectFactory;

import java.util.Map;

import org.everit.json.schema.Schema;
import org.junit.Test;

public class ExampleCatalogLoaderIT {

    private final CatalogLoader catalogLoader = new ObjectFactory().catalogLoader();

    @Test
    public void shouldMapSchemasOnClasspathToTheirIds2() throws Exception {

        final Map<String, Schema> idsToSchemaMap = catalogLoader.loadCatalogsFromClasspath();

        assertThat(idsToSchemaMap.size(), is(2));

        final String id_1 = "http://justice.gov.uk/example/standard/ingredient.json";
        final String json_1 = idsToSchemaMap.get(id_1).toString();

        final String id_2 = "http://justice.gov.uk/example/cakeshop/example.add-recipe.json";
        final String json_2 = idsToSchemaMap.get(id_2).toString();
    }
}
