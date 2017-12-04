package uk.gov.justice.schema.catalog;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class CatalogProducer {

    @Produces
    public Catalog catalog() {
        return new CatalogObjectFactory().catalog();
    }
}
