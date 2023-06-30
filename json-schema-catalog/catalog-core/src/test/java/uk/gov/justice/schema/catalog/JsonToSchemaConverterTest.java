package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.client.LocalFileSystemSchemaClient;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JsonToSchemaConverterTest {

    @InjectMocks
    private JsonToSchemaConverter jsonToSchemaConverter;

    @Test
    public void shouldLoadASchemaWhichIncludesALocallyStoredSchemaFragmentFromJsonAsString() throws Exception {

        final URL mainSchemaUrl = getClass().getClassLoader().getResource("json/schema/context/person.json");
        final URL schemaFragmentUrl = getClass().getClassLoader().getResource("json/schema/standards/complex_address.json");

        final URL jsonToVerifyUrl = getClass().getClassLoader().getResource("json/person.json");

        assertThat(mainSchemaUrl, is(notNullValue()));
        assertThat(schemaFragmentUrl, is(notNullValue()));
        assertThat(jsonToVerifyUrl, is(notNullValue()));

        final LocalFileSystemSchemaClient localFileSystemSchemaClient = mock(LocalFileSystemSchemaClient.class);

        try (final InputStream inputStream = schemaFragmentUrl.openStream()) {

            when(localFileSystemSchemaClient.get("http://justice.gov.uk/standards/complex_address.json")).thenReturn(inputStream);

            final String schemaJson = IOUtils.toString(mainSchemaUrl);
            final Schema schema = jsonToSchemaConverter.convert(schemaJson, localFileSystemSchemaClient);

            final String json = IOUtils.toString(jsonToVerifyUrl);

            schema.validate(new JSONObject(json));
        }
    }

    @Test
    public void shouldLoadASchemaWhichIncludesALocallyStoredSchemaFragmentFromJsonObject() throws Exception {

        final URL mainSchemaUrl = getClass().getClassLoader().getResource("json/schema/context/person.json");
        final URL schemaFragmentUrl = getClass().getClassLoader().getResource("json/schema/standards/complex_address.json");

        final URL jsonToVerifyUrl = getClass().getClassLoader().getResource("json/person.json");

        assertThat(mainSchemaUrl, is(notNullValue()));
        assertThat(schemaFragmentUrl, is(notNullValue()));
        assertThat(jsonToVerifyUrl, is(notNullValue()));

        final LocalFileSystemSchemaClient localFileSystemSchemaClient = mock(LocalFileSystemSchemaClient.class);

        try (final InputStream inputStream = schemaFragmentUrl.openStream()) {

            when(localFileSystemSchemaClient.get("http://justice.gov.uk/standards/complex_address.json")).thenReturn(inputStream);

            final String schemaJson = IOUtils.toString(mainSchemaUrl);

            final Schema schema = jsonToSchemaConverter.convert(new JSONObject(schemaJson), localFileSystemSchemaClient);

            final String json = IOUtils.toString(jsonToVerifyUrl);

            schema.validate(new JSONObject(json));
        }
    }

    @Test
    public void shouldFailWithErrorMessageIfParsingTheJsonStringFails() throws Exception {

        final URL url = getClass().getClassLoader().getResource("json/dodgy-schemas/schema-with-two-ids.json");

        assertThat(url, is(notNullValue()));

        final String schemaJson = IOUtils.toString(url);

        final SchemaCatalogException expected = assertThrows(
                SchemaCatalogException.class,
                () -> jsonToSchemaConverter.convert(schemaJson, mock(LocalFileSystemSchemaClient.class)));

                assertThat(expected.getCause(), is(instanceOf(JSONException.class)));
        assertThat(expected.getMessage(), startsWith("Failed to convert schema json to Schema Object. Schema json:"));
    }
}
