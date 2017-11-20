package uk.gov.justice.schema.catalog.generation;

import uk.gov.justice.schema.catalog.SchemaCatalogException;

public class CatalogGenerationException extends SchemaCatalogException {

    public CatalogGenerationException(final String message) {
        super(message);
    }

    public CatalogGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
