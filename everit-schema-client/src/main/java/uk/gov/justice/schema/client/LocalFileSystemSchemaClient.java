package uk.gov.justice.schema.client;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.everit.json.schema.loader.SchemaClient;

public class LocalFileSystemSchemaClient implements SchemaClient {

    private final Map<String, String> urlsToJson;

    public LocalFileSystemSchemaClient(final Map<String, String> urlsToJson) {
        this.urlsToJson = urlsToJson;
    }

    @Override
    public InputStream get(final String url) {

        if (urlsToJson.containsKey(url)) {
            return new ByteArrayInputStream(urlsToJson.get(url).getBytes(UTF_8));
        }

        throw new SchemaClientException(format("Failed to find schema with id '%s'", url));
    }
}
