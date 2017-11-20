package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.CatalogLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogLoaderProducerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CatalogLoaderProducer catalogLoaderProducer;

    @Test
    public void shouldProduceCatalogLoader() throws Exception {
        final CatalogLoader catalogLoader = catalogLoaderProducer.createCatalogLoader();

        assertThat(catalogLoader, is(notNullValue()));
    }
}