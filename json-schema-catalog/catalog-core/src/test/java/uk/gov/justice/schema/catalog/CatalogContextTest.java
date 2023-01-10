package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogContextTest {

    @InjectMocks
    private CatalogContext catalogContext;

    @Test
    public void shouldGetCatalogFilename() throws Exception {
        assertThat(catalogContext.getCatalogFilename(), is("schema_catalog.json"));
    }

    @Test
    public void shouldGetTheCatalogLocation() throws Exception {
        assertThat(catalogContext.getCatalogLocation(), is("META-INF/"));
    }

    @Test
    public void shouldGetTheCatalogFullPath() throws Exception {
        assertThat(catalogContext.getCatalogFullPath(), is("META-INF/schema_catalog.json"));
    }
}
