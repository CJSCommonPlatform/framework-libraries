package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogProducerTest {

    @InjectMocks
    private CatalogProducer catalogProducer;

    @Test
    public void shouldCreateTheCatalog() throws Exception {

        final Catalog catalog = catalogProducer.catalog();

        assertThat(catalog, is(notNullValue()));

        assertThat(getPrivateField(catalog, "rawCatalog", RawCatalog.class), is(notNullValue()));
        assertThat(getPrivateField(catalog, "schemaClientFactory", SchemaClientFactory.class), is(notNullValue()));
        assertThat(getPrivateField(catalog, "jsonStringToSchemaConverter", JsonStringToSchemaConverter.class), is(notNullValue()));
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(final Catalog catalog, final String fieldName, @SuppressWarnings("unused") final Class<T> clazz) throws Exception {

        final Field field = catalog.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(catalog);
    }
}
