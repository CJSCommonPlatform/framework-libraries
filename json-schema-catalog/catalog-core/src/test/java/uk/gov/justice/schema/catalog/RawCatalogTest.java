package uk.gov.justice.schema.catalog;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RawCatalogTest {

    @Mock
    private JsonSchemaFileLoader jsonSchemaFileLoader;

    @InjectMocks
    private RawCatalog rawCatalog;

    @Spy
    private CatalogUpdater catalogUpdater;

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

        assertThat(rawCatalog.getRawJsonSchema(schemaId), is(empty()));
    }

    @Test
    public void shouldUpdateCatalogSchemaWithPaths() throws Exception {
        final Path basePath = Paths.get((""));
        final Path aliasJsonPath = Paths.get(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.alias.json").toURI());
        final Path personJson = Paths.get(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.person-updated.json").toURI());
        final List<Path> paths = asList(aliasJsonPath, personJson);

        final String expectedSchema = IOUtils.toString(personJson.toUri(), UTF_8);
        final String schemaId = "http://justice.gov.uk/standards/example.events.person-updated.json";

        rawCatalog.updateCatalogSchemaCache(basePath, paths);

        assertThat(rawCatalog.getRawJsonSchema(schemaId), is(of(expectedSchema)));
    }
}
