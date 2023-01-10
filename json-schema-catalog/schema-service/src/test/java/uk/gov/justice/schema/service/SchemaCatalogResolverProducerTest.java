package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.schema.catalog.JsonToSchemaConverter;
import uk.gov.justice.schema.catalog.RawCatalog;
import uk.gov.justice.schema.catalog.SchemaCatalogResolver;
import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaCatalogResolverProducerTest {

    @InjectMocks
    private SchemaCatalogResolverProducer schemaCatalogResolverProducer;

    @Test
    public void shouldCreateTheCatalog() throws Exception {

        final SchemaCatalogResolver schemaCatalogResolver = schemaCatalogResolverProducer.schemaCatalogResolver();

        assertThat(schemaCatalogResolver, is(notNullValue()));

        assertThat(getPrivateField(schemaCatalogResolver, "rawCatalog", RawCatalog.class), is(notNullValue()));
        assertThat(getPrivateField(schemaCatalogResolver, "schemaClientFactory", SchemaClientFactory.class), is(notNullValue()));
        assertThat(getPrivateField(schemaCatalogResolver, "jsonStringToSchemaConverter", JsonToSchemaConverter.class), is(notNullValue()));
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(final SchemaCatalogResolver schemaCatalogResolver, final String fieldName, @SuppressWarnings("unused") final Class<T> clazz) throws Exception {

        final Field field = schemaCatalogResolver.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(schemaCatalogResolver);
    }
}
