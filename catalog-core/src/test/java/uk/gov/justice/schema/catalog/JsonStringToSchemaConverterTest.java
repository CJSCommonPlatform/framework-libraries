package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.client.LocalFileSystemSchemaClient;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonStringToSchemaConverterTest {

    @InjectMocks
    private JsonStringToSchemaConverter jsonStringToSchemaConverter;

    @Test
    public void shouldLoadASchemaWhichIncludesALocallyStoredSchemaFragment() throws Exception {

        final URL mainSchemaUrl = getClass().getClassLoader().getResource("json/schema/context/person.json");
        final URL schemaFragmentUrl = getClass().getClassLoader().getResource("json/schema/standards/complex_address.json");

        final URL jsonToVerifyUrl = getClass().getClassLoader().getResource("json/person.json");

        assertThat(mainSchemaUrl, is(notNullValue()));
        assertThat(schemaFragmentUrl, is(notNullValue()));
        assertThat(jsonToVerifyUrl, is(notNullValue()));

        final LocalFileSystemSchemaClient localFileSystemSchemaClient = mock(LocalFileSystemSchemaClient.class);

        try(final InputStream inputStream = schemaFragmentUrl.openStream()) {

            when(localFileSystemSchemaClient.get("http://justice.gov.uk/standards/complex_address.json")).thenReturn(inputStream);

            final String schemaJson = IOUtils.toString(mainSchemaUrl);
            final Schema schema = jsonStringToSchemaConverter.convert(schemaJson, localFileSystemSchemaClient);

            final String json = IOUtils.toString(jsonToVerifyUrl);

            schema.validate(new JSONObject(json));
        }
    }

    @Test
    public void shouldFailWithErrorMessageIfParsingTheJsonStringFails() throws Exception {

        final URL url = getClass().getClassLoader().getResource("json/dodgy-schemas/schema-with-two-ids.json");

        assertThat(url, is(notNullValue()));

        final String schemaJson = IOUtils.toString(url);

        try {
            jsonStringToSchemaConverter.convert(schemaJson, mock(LocalFileSystemSchemaClient.class));
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(instanceOf(JSONException.class)));
            assertThat(expected.getMessage(), startsWith("Failed to convert schema json to Schema Object. Schema json:"));
        }
    }
}
