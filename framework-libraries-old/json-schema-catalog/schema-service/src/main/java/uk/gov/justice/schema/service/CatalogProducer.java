package uk.gov.justice.schema.service;

import uk.gov.justice.schema.catalog.Catalog;
import uk.gov.justice.schema.catalog.CatalogObjectFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * A Producer for the {@link Catalog} should you be running in a CDI container
 */
@ApplicationScoped
public class CatalogProducer {

    /**
     * @return a new instance of {@link Catalog}
     */
    @Produces
    public Catalog catalog() {
        return new CatalogObjectFactory().catalog();
    }
}
