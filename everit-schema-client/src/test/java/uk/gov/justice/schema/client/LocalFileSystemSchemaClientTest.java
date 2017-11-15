package uk.gov.justice.schema.client;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.JsonAssert.with;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class LocalFileSystemSchemaClientTest {

    @Test
    public void shouldFindSchemaJsonByItsUrlIdAndReturnAsAnInputStream() throws Exception {

        final Map<String, String> urlsToJson = of("url", "{\"some\": \"json\"}");

        final LocalFileSystemSchemaClient localFileSystemSchemaClient = new LocalFileSystemSchemaClient(urlsToJson);

        final InputStream inputStream = localFileSystemSchemaClient.get("url");

        final String json = IOUtils.toString(inputStream, UTF_8);

        with(json)
                .assertThat("$.some", is("json"))
        ;

        inputStream.close();
    }

    @Test
    public void shouldFailIfNoSchemaFoundForTheUrlId() throws Exception {

        final String url = "file:/some/url/or/other";

        final Map<String, String> urlsToJson = new HashMap<>();

        final LocalFileSystemSchemaClient localFileSystemSchemaClient = new LocalFileSystemSchemaClient(urlsToJson);

        try {
            localFileSystemSchemaClient.get(url);
            fail();
        } catch (final SchemaClientException expected) {
            assertThat(expected.getMessage(), is("Failed to find schema with id 'file:/some/url/or/other'"));
        }
    }
}
