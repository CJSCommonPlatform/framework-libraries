package uk.gov.justice.schema.catalog.client;

import uk.gov.justice.schema.catalog.RawCatalog;

import org.everit.json.schema.loader.SchemaClient;

public class SchemaClientFactory {

    public SchemaClient create(final RawCatalog rawCatalog) {
        return new LocalFileSystemSchemaClient(rawCatalog);
    }
}
