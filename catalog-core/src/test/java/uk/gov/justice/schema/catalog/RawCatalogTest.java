package uk.gov.justice.schema.catalog;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RawCatalogTest {

    @Mock
    private JsonSchemaFileLoader jsonSchemaFileLoader;

    @InjectMocks
    private RawCatalog rawCatalog;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGetRawJsonSchemaFromTheLoadedSchemaCache() throws Exception {

        final String schemaId = "schema id";
        final String rawJsonSchema = "json schema";

        final Map<String, String> schemaIdsToRawJsonSchemaCache = mock(Map.class);

        when(jsonSchemaFileLoader.loadSchemas()).thenReturn(schemaIdsToRawJsonSchemaCache);
        when(schemaIdsToRawJsonSchemaCache.get(schemaId)).thenReturn(rawJsonSchema);

        rawCatalog.initialize();

        assertThat(rawCatalog.getRawJsonSchema(schemaId), is(of(rawJsonSchema)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnEmptyIfNoSchemaFound() throws Exception {
        final String schemaId = "schema id";

        final Map<String, String> schemaIdsToRawJsonSchemaCache = mock(Map.class);

        when(jsonSchemaFileLoader.loadSchemas()).thenReturn(schemaIdsToRawJsonSchemaCache);
        when(schemaIdsToRawJsonSchemaCache.get(schemaId)).thenReturn(null);

        rawCatalog.initialize();

        assertThat(rawCatalog.getRawJsonSchema(schemaId), is(empty()));
    }

    @Test
    public void shouldCacheTheResultsOfTheCallToJsonSchemaFileLoader() throws Exception {

        final String schemaId = "schema id";
        final String rawJsonSchema = "json schema";

        final Map<String, String> schemaIdsToRawJsonSchemaCache = mock(Map.class);

        when(jsonSchemaFileLoader.loadSchemas()).thenReturn(schemaIdsToRawJsonSchemaCache);
        when(schemaIdsToRawJsonSchemaCache.get(schemaId)).thenReturn(rawJsonSchema);

        rawCatalog.initialize();

        rawCatalog.getRawJsonSchema(schemaId);
        rawCatalog.getRawJsonSchema(schemaId);
        rawCatalog.getRawJsonSchema(schemaId);
        rawCatalog.getRawJsonSchema(schemaId);
        rawCatalog.getRawJsonSchema(schemaId);
        rawCatalog.getRawJsonSchema(schemaId);

        verify(jsonSchemaFileLoader, times(1)).loadSchemas();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnEmptyIfTheCacheIsNotInitialized() throws Exception {
        final String schemaId = "schema id";

        final Map<String, String> schemaIdsToRawJsonSchemaCache = mock(Map.class);

        when(jsonSchemaFileLoader.loadSchemas()).thenReturn(schemaIdsToRawJsonSchemaCache);
        when(schemaIdsToRawJsonSchemaCache.get(schemaId)).thenReturn(null);

        assertThat(rawCatalog.getRawJsonSchema(schemaId), is(empty()));
    }
}
