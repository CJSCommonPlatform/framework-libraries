package uk.gov.justice.schema.catalog;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.client.LocalFileSystemSchemaClient;
import uk.gov.justice.schema.client.SchemaClientFactory;

import java.net.URL;
import java.util.Map;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogLoaderTest {


    @Mock
    private SchemaResolverAndLoader schemaResolverAndLoader;

    @Mock
    private CatalogToSchemaResolver catalogToSchemaResolver;

    @Mock
    private JsonSchemaLoader jsonSchemaLoader;

    @Mock
    private SchemaClientFactory schemaClientFactory;

    @InjectMocks
    private CatalogLoader catalogLoader;

    @Test
    public void shouldLoadCatalogs() throws Exception {

        final Map<String, URL> schemaLocationMap = of("name", new URL("file://src/main/file.txt"));
        final Map<String, String> urlsToJson = of("id", "{\"some\": \"json\"}");
        final Map<String, Schema> urlsToSchema = of("id", mock(Schema.class));

        final LocalFileSystemSchemaClient localFileSystemSchemaClient = mock(LocalFileSystemSchemaClient.class);

        when(catalogToSchemaResolver.resolveSchemaLocations()).thenReturn(schemaLocationMap);
        when(jsonSchemaLoader.loadJsonFrom(schemaLocationMap)).thenReturn(urlsToJson);
        when(schemaClientFactory.create(urlsToJson)).thenReturn(localFileSystemSchemaClient);

        when(schemaResolverAndLoader.loadSchemas(urlsToJson, localFileSystemSchemaClient)).thenReturn(urlsToSchema);

        assertThat(catalogLoader.loadCatalogsFromClasspath(), is(urlsToSchema));
    }
}
