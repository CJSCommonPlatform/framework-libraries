package uk.gov.justice.schema.catalog;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

        final Map<String, String> schemaIdsToRawJsonSchemaCache = mock(Map.class);

        when(jsonSchemaFileLoader.loadSchemas()).thenReturn(schemaIdsToRawJsonSchemaCache);
        when(schemaIdsToRawJsonSchemaCache.get(schemaId)).thenReturn(null);

        assertThat(rawCatalog.getRawJsonSchema(schemaId), is(empty()));
    }

    @Test
    public void testShouldUpdateCatalogSchemaWithPaths(){
        final Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();
        final Path basePath = Paths.get((""));

        final String schemaId = "http://justice.gov.uk/standards/example.events.person-updated.json";

        schemaIdsToRawJsonSchemaCache.put("http://justice.gov.uk/standards/address.json", "json schema" );

        rawCatalog.initialize();
        Collection<Path> paths = new ArrayList<>();

        try {
            final File aliasJson = new File(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.alias.json").toURI());
            final File personJson = new File(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.person-updated.json").toURI());
            paths.add(Paths.get(aliasJson.toURI()));
            paths.add(Paths.get(personJson.toURI()));
            rawCatalog.updateCatalogSchemaCache(basePath, paths);

            final String schema = IOUtils.toString(personJson.toURI().toURL(), UTF_8);
            assertThat(rawCatalog.getRawJsonSchema(schemaId), is(of(schema)));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
