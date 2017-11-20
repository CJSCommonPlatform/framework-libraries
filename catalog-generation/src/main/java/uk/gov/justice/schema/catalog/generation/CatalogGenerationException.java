package uk.gov.justice.schema.catalog.generation;

public class CatalogGenerationException extends RuntimeException {

    public CatalogGenerationException(final String message) {
        super(message);
    }

    public CatalogGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
