package uk.gov.justice.schema.service;

import uk.gov.justice.schema.catalog.CatalogLoader;

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.everit.json.schema.Schema;

@ApplicationScoped
public class SchemaCatalogService {

    @Inject
    private CatalogLoader catalogLoader;

    private Map<String, Schema> catalog;

    @PostConstruct
    public void initialiseCatalog() {
        catalog = catalogLoader.loadCatalogsFromClasspath();
    }

    public Optional<Schema> findSchema(final String uri) {
        return Optional.ofNullable(catalog.get(uri));
    }
}
