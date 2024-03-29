package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.schema.catalog.Catalog;
import uk.gov.justice.schema.catalog.JsonToSchemaConverter;
import uk.gov.justice.schema.catalog.RawCatalog;
import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CatalogProducerTest {

    @InjectMocks
    private CatalogProducer catalogProducer;

    @Test
    public void shouldCreateTheCatalog() throws Exception {

        final Catalog catalog = catalogProducer.catalog();

        assertThat(catalog, is(notNullValue()));

        assertThat(getPrivateField(catalog, "rawCatalog", RawCatalog.class), is(notNullValue()));
        assertThat(getPrivateField(catalog, "schemaClientFactory", SchemaClientFactory.class), is(notNullValue()));
        assertThat(getPrivateField(catalog, "jsonToSchemaConverter", JsonToSchemaConverter.class), is(notNullValue()));
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(final Catalog catalog, final String fieldName, @SuppressWarnings("unused") final Class<T> clazz) throws Exception {

        final Field field = catalog.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(catalog);
    }
}
