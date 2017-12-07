package uk.gov.justice.schema.catalog.client;

import uk.gov.justice.schema.catalog.RawCatalog;

import org.everit.json.schema.loader.SchemaClient;

/**
 * Creates a new instance of {@link LocalFileSystemSchemaClient}. 
 */
public class SchemaClientFactory {

    /**
     * Creates a new instance of {@link LocalFileSystemSchemaClient}
     *
     * @param rawCatalog The {@link RawCatalog} containing the mapping of Schema ids
     *                   to their location on the file system
     * @return A new instance of {@link LocalFileSystemSchemaClient}
     */
    public SchemaClient create(final RawCatalog rawCatalog) {
        return new LocalFileSystemSchemaClient(rawCatalog);
    }
}
