package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.loader.SchemaClient;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JsonStringToSchemaConverterTest {

    @InjectMocks
    private JsonStringToSchemaConverter jsonStringToSchemaConverter;

    @Test
    public void shouldFailWithErrorMessageIfParsingTheJsonStringFails() throws Exception {

        final URL url = getClass().getClassLoader().getResource("json/dodgy-schemas/schema-with-two-ids.json");

        assertThat(url, is(notNullValue()));

        final String schemaJson = IOUtils.toString(url);

        try {
            jsonStringToSchemaConverter.convert(schemaJson, mock(SchemaClient.class));
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(instanceOf(JSONException.class)));
            assertThat(expected.getMessage(), startsWith("Failed to convert schema json to Schema Object. Schema json:"));
        }
    }
}
