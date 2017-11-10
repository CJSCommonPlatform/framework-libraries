package uk.gov.justice.schema.catalog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


//@RunWith(MockitoJUnitRunner.class)
@RunWith(MockitoJUnitRunner.class)
public class CatalogLoaderTest {

    @Mock
    SchemaDictionary schemaDictionary;

    @Mock
    SchemaLoader schemaLoader;


    @InjectMocks
    CatalogLoader catalogLoader;


    @Test
    public void shouldLoadCatalogs() throws Exception {
        catalogLoader.loadCatalogsFromClasspath();

    }
}