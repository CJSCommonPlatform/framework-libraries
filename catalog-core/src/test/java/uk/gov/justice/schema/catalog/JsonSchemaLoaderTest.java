package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonSchemaLoaderTest {

    @Mock
    private FileContentsAsStringLoader fileContentsAsStringLoader;

    @InjectMocks
    private JsonSchemaLoader jsonSchemaLoader;

    @Test
    public void shouldName() throws Exception {

        final URL url_1 = new URL("file:/my/json/schema.json");
        final URL url_2 = new URL("file:/my/other/json/schema.json");

        final String json_1 = "{\"some\": \"json\"}";
        final String json_2 = "{\"other\": \"json\"}";

        final Map<String, URL> schemaLocationMap = ImmutableMap.of("id_1", url_1, "id_2", url_2);

        when(fileContentsAsStringLoader.readFileContents(url_1)).thenReturn(json_1);
        when(fileContentsAsStringLoader.readFileContents(url_2)).thenReturn(json_2);

        final Map<String, String> idsToJson = jsonSchemaLoader.loadJsonFrom(schemaLocationMap);

        assertThat(idsToJson.get("id_1"), is(json_1));
        assertThat(idsToJson.get("id_2"), is(json_2));
    }
}
