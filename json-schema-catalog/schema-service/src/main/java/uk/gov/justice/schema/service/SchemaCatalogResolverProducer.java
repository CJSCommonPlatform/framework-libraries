package uk.gov.justice.schema.service;

import uk.gov.justice.schema.catalog.CatalogObjectFactory;
import uk.gov.justice.schema.catalog.SchemaCatalogResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * A Producer for the {@link SchemaCatalogResolver} should you be running in a CDI container
 */
@ApplicationScoped
public class SchemaCatalogResolverProducer {

    /**
     * @return a new instance of {@link SchemaCatalogResolver}
     */
    @Produces
    public SchemaCatalogResolver schemaCatalogResolver() {
        return new CatalogObjectFactory().schemaCatalogResolver();
    }
}
