package uk.gov.justice.schema.catalog.client;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import uk.gov.justice.schema.catalog.RawCatalog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.everit.json.schema.loader.SchemaClient;

public class LocalFileSystemSchemaClient implements SchemaClient {

    private final RawCatalog rawCatalog;

    public LocalFileSystemSchemaClient(final RawCatalog rawCatalog) {
        this.rawCatalog = rawCatalog;
    }

    @Override
    public InputStream get(final String schemaId) {

        final String rawJsonSchema = rawCatalog.getRawJsonSchema(schemaId)
                .orElseThrow(() -> new SchemaClientException(format("Failed to find schema with id '%s'", schemaId)));

        return new ByteArrayInputStream(rawJsonSchema.getBytes(UTF_8));
    }
}
