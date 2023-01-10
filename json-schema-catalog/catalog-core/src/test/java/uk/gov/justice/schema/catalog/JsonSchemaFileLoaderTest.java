package uk.gov.justice.schema.catalog;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonSchemaFileLoaderTest {

    @Mock
    private FileContentsAsStringLoader fileContentsAsStringLoader;

    @Mock
    private CatalogToSchemaResolver catalogToSchemaResolver;

    @InjectMocks
    private JsonSchemaFileLoader jsonSchemaFileLoader;

    @Test
    public void shouldFindAllTheSchemasThenLoadThemAsAString() throws Exception {

        final URL url_1 = new URL("file:/my/json/schema.json");
        final URL url_2 = new URL("file:/my/other/json/schema.json");

        final String json_1 = "{\"some\": \"json\"}";
        final String json_2 = "{\"other\": \"json\"}";

        final Map<String, URL> schemaLocationMap = of("id_1", url_1, "id_2", url_2);

        when(catalogToSchemaResolver.resolveSchemaLocations()).thenReturn(schemaLocationMap);
        when(fileContentsAsStringLoader.readFileContents(url_1)).thenReturn(json_1);
        when(fileContentsAsStringLoader.readFileContents(url_2)).thenReturn(json_2);

        final Map<String, String> idsToJson = jsonSchemaFileLoader.loadSchemas();

        assertThat(idsToJson.get("id_1"), is(json_1));
        assertThat(idsToJson.get("id_2"), is(json_2));
    }
}
