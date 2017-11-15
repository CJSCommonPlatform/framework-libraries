package uk.gov.justice.schema.catalog.generation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogGenerationContextTest {

    @InjectMocks
    private CatalogGenerationContext catalogGenerationContext;

    @Test
    public void shouldGetCatalogGenerationRoot() throws Exception {
        assertThat(catalogGenerationContext.getCatalogGenerationRoot(), is("target/generated-catalogs"));
    }

    @Test
    public void shouldGetCatalogPath() throws Exception {
        assertThat(catalogGenerationContext.getCatalogPath(), is("json/schema/catalog"));
    }

    @Test
    public void shouldGetCatalogFilename() throws Exception {
        assertThat(catalogGenerationContext.getCatalogFilename(), is("schema_catalog.json"));
    }

    @Test
    public void shouldGetJsonSchemaPath() throws Exception {
        assertThat(catalogGenerationContext.getJsonSchemaPath(), is("raml/json/schema/"));
    }
}
