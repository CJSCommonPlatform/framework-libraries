package uk.gov.justice.schema.catalog;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;

public class CatalogToSchemaResolver {

    private final ClasspathCatalogLoader classpathCatalogLoader;
    private final SchemaResolver schemaResolver;
    private final Logger logger;

    @Inject
    public CatalogToSchemaResolver(
            final ClasspathCatalogLoader classpathCatalogLoader,
            final SchemaResolver schemaResolver,
            final Logger logger) {
        this.classpathCatalogLoader = classpathCatalogLoader;
        this.schemaResolver = schemaResolver;
        this.logger = logger;
    }


    /**
     *
     * @return Mapping from schemaId to a schema location url
     */
    public Map<String, URL> resolveSchemaLocations() {

        final Map<String, URL> schemaLocations = new HashMap<>();

        final Map<URI, Catalog> catalogs = classpathCatalogLoader.getCatalogs();

        for(final URI catalogLocation: catalogs.keySet()) {
            for(final Group group: catalogs.get(catalogLocation).getGroup()) {
                for(final Schema schema: group.getSchema()) {
                    final String location = schema.getLocation();
                    final Optional<String> baseLocation = ofNullable(group.getBaseLocation());

                    final String schemaId = schema.getId();
                    final URL schemaLocationUrl = schemaResolver.resolve(catalogLocation, location, baseLocation);

                    if(schemaLocations.containsKey(schemaId)) {
                        final URL otherLocation = schemaLocations.get(schemaId);
                        logger.warn(format("Found duplicate schema id '%s' for schemaLocations '%s' and '%s'", schemaId, otherLocation, schemaLocationUrl));
                    } else {
                        schemaLocations.put(schemaId, schemaLocationUrl);
                    }
                }
            }
        }

        return schemaLocations;
    }
}
