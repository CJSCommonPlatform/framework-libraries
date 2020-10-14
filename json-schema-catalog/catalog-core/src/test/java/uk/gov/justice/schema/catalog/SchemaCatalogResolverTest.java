package uk.gov.justice.schema.catalog;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaCatalogResolverTest {

    @Mock
    private RawCatalog rawCatalog;

    @Mock
    private SchemaClientFactory schemaClientFactory;

    @Mock
    private JsonToSchemaConverter jsonStringToSchemaConverter;

    @InjectMocks
    private SchemaCatalogResolver schemaCatalogResolver;

    @Test
    public void shouldResolveSchema() {
        final Schema schema = mock(Schema.class);
        final JSONObject jsonSchema = mock(JSONObject.class);
        final SchemaClient schemaClient = mock(SchemaClient.class);

        when(schemaClientFactory.create(rawCatalog)).thenReturn(schemaClient);
        when(jsonStringToSchemaConverter.convert(jsonSchema, schemaClient)).thenReturn(schema);

        final Schema resultSchema = schemaCatalogResolver.loadSchema(jsonSchema);

        assertThat(resultSchema, is(schema));
    }

    @Test
    public void shouldUpdateRawCatalogWithPaths() {
        final Path basePath = Paths.get("");
        final List<Path> paths = singletonList(Paths.get("test/path"));

        schemaCatalogResolver.updateCatalogSchemaCache(basePath, paths);

        verify(rawCatalog).updateCatalogSchemaCache(basePath, paths);
    }
}