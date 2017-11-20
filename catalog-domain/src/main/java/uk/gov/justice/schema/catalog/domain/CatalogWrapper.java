package uk.gov.justice.schema.catalog.domain;

public class CatalogWrapper {

    private final Catalog catalog;

    public CatalogWrapper(final Catalog catalog) {
        this.catalog = catalog;
    }

    public Catalog getCatalog() {
        return catalog;
    }

}
