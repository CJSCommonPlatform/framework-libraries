package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.Catalog;

import java.util.Optional;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("ConstantConditions")
@RunWith(MockitoJUnitRunner.class)
public class SchemaCatalogServiceTest {

    @Mock
    private Schema catalogSchema;

    @Mock
    private Catalog catalog;

    @InjectMocks
    private SchemaCatalogService schemaCatalogService;

    @Test
    public void shouldReturnSchemaForSchemaId() throws Exception {
        final String schemaId = "schemaId";

        when(catalog.getSchema(schemaId)).thenReturn(Optional.of(catalogSchema));

        final Optional<Schema> schema = schemaCatalogService.findSchema(schemaId);

        assertThat(schema.isPresent(), is(true));
        assertThat(schema.get(), is(catalogSchema));
    }

    @Test
    public void shouldReturnOptionalEmptyIfSchemaIdIsUnknown() throws Exception {
        final String schemaId = "unknown";

        when(catalog.getSchema(schemaId)).thenReturn(Optional.empty());

        final Optional<Schema> schema = schemaCatalogService.findSchema(schemaId);

        assertThat(schema.isPresent(), is(false));
    }

    @Test
    public void shouldCacheTheResultsOfTheCallToCatalog() throws Exception {
        final String schemaId = "schemaId";

        when(catalog.getSchema(schemaId)).thenReturn(Optional.of(catalogSchema));

        assertThat(schemaCatalogService.findSchema(schemaId).get(), is(catalogSchema));
        assertThat(schemaCatalogService.findSchema(schemaId).get(), is(catalogSchema));
        assertThat(schemaCatalogService.findSchema(schemaId).get(), is(catalogSchema));
        assertThat(schemaCatalogService.findSchema(schemaId).get(), is(catalogSchema));
        assertThat(schemaCatalogService.findSchema(schemaId).get(), is(catalogSchema));

        verify(catalog, times(1)).getSchema(schemaId);
    }
}
