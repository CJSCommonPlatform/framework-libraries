package uk.gov.justice.schema.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.CatalogLoader;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import org.everit.json.schema.Schema;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaCatalogServiceTest {

    @Mock
    private Schema catalogSchema;

    @Mock
    private CatalogLoader catalogLoader;

    @InjectMocks
    private SchemaCatalogService schemaCatalogService;

    @Before
    public void setup() {
        final Map<String, Schema> catalog = ImmutableMap.of("uri", catalogSchema);

        when(catalogLoader.loadCatalogsFromClasspath()).thenReturn(catalog);
        schemaCatalogService.initialiseCatalog();
    }

    @Test
    public void shouldReturnSchemaForUriId() throws Exception {
        final String uri = "uri";

        final Optional<Schema> schema = schemaCatalogService.findSchema(uri);

        assertThat(schema.isPresent(), is(true));
        assertThat(schema.get(), is(catalogSchema));
    }

    @Test
    public void shouldReturnOptionalEmptyIfUriIdIsUnknown() throws Exception {
        final String uri = "unknown";

        final Optional<Schema> schema = schemaCatalogService.findSchema(uri);

        assertThat(schema.isPresent(), is(false));
    }
}