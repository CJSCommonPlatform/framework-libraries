package uk.gov.justice.schema.catalog.client;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.RawCatalog;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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

        final SchemaClientException expected =
                assertThrows(SchemaClientException.class, () -> localFileSystemSchemaClient.get(schemaId));

        assertThat(expected.getMessage(), is("Failed to find schema with id 'schemaId'"));
    }
}
