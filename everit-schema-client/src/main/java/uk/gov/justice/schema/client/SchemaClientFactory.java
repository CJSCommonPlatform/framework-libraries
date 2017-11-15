package uk.gov.justice.schema.client;

import java.util.Map;

import org.everit.json.schema.loader.SchemaClient;

public class SchemaClientFactory {

    public SchemaClient create(final Map<String, String> urlsToJson) {
        return new LocalFileSystemSchemaClient(urlsToJson);
    }
}
