package uk.gov.justice;

import uk.gov.justice.schema.catalog.CatalogLoader;
import uk.gov.justice.schema.catalog.SchemaDictionary;
import uk.gov.justice.schema.catalog.SchemaLoader;
import uk.gov.justice.schema.catalog.pojo.Catalog;

import javax.inject.Inject;

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