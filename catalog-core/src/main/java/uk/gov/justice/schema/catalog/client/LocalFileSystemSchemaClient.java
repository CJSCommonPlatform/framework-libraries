package uk.gov.justice.schema.catalog.client;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import uk.gov.justice.schema.catalog.RawCatalog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.everit.json.schema.loader.SchemaClient;

/**
 * An implementation of the <a href="https://github.com/everit-org/json-schema">Everit Json Validator</a>
 * SchemaClient.
 *
 * Rather than returning a schema at the uri specified by its schema id. It returns a
 * Schema mapped by the schema id on the local file system
 *
 */
public class LocalFileSystemSchemaClient implements SchemaClient {

    private final RawCatalog rawCatalog;

    public LocalFileSystemSchemaClient(final RawCatalog rawCatalog) {
        this.rawCatalog = rawCatalog;
    }

    /**
     * Returns an {@link InputStream} to a locally stored Schema file that has been
     * mapped to its Schema id
     *
     * @param schemaId The id of the Schema
     *
     * @return An {@link InputStream} to the locally stored Schema
     */
    @Override
    public InputStream get(final String schemaId) {

        final String rawJsonSchema = rawCatalog.getRawJsonSchema(schemaId)
                .orElseThrow(() -> new SchemaClientException(format("Failed to find schema with id '%s'", schemaId)));

        return new ByteArrayInputStream(rawJsonSchema.getBytes(UTF_8));
    }
}
