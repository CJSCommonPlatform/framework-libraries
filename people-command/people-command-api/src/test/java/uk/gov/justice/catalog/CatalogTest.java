package uk.gov.justice.catalog;

import uk.gov.justice.schema.catalog.CatalogLoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogTest {

    @Test
    public void testCatalogLoad() {

        CatalogLoader catalogLoader = new CatalogLoader();
        catalogLoader.loadCatalogsFromClasspath();

    }
}
