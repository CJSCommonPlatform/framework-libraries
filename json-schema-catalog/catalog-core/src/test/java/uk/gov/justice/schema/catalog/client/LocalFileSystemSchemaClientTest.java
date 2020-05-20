package uk.gov.justice.schema.catalog.client;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.RawCatalog;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalFileSystemSchemaClientTest {

    @Mock
    private RawCatalog rawCatalog;

    @InjectMocks
    private LocalFileSystemSchemaClient localFileSystemSchemaClient;

    @Test
    public void shouldFindSchemaJsonByItsUrlIdAndReturnAsAnInputStream() throws Exception {

        final String schemaId = "schemaId";
        final String rawJsonSchema = "{\"some\": \"json\"}";

        when(rawCatalog.getRawJsonSchema(schemaId)).thenReturn(of(rawJsonSchema));

        final InputStream inputStream = localFileSystemSchemaClient.get(schemaId);

        final String json = IOUtils.toString(inputStream, UTF_8);

        with(json)
                .assertThat("$.some", is("json"))
        ;

        inputStream.close();
    }

    @Test
    public void shouldFailIfNoSchemaFoundForTheUrlId() throws Exception {

        final String schemaId = "schemaId";

        when(rawCatalog.getRawJsonSchema(schemaId)).thenReturn(empty());

        try {
            localFileSystemSchemaClient.get(schemaId);
            fail();
        } catch (final SchemaClientException expected) {
            assertThat(expected.getMessage(), is("Failed to find schema with id 'schemaId'"));
        }
    }
}
