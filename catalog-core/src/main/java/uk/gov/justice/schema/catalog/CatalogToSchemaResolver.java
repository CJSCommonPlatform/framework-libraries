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

import org.slf4j.Logger;

/**
 * Finds all catalog files on the classpath, then returns a mapping of the schema id to its
 * location.
 */
public class CatalogToSchemaResolver {

    private final ClasspathCatalogLoader classpathCatalogLoader;
    private final SchemaResolver schemaResolver;
    private final Logger logger;

    public CatalogToSchemaResolver(final ClasspathCatalogLoader classpathCatalogLoader,
                                   final SchemaResolver schemaResolver,
                                   final Logger logger) {
        this.classpathCatalogLoader = classpathCatalogLoader;
        this.schemaResolver = schemaResolver;
        this.logger = logger;
    }

    /**
     * Find all catalog files on the classpath and return a mapping of the schema id to
     * the location of the schema file
     *
     * @return Mapping from schemaId to a schema location url
     */
    public Map<String, URL> resolveSchemaLocations() {

        final Map<String, URL> schemaLocations = new HashMap<>();

        final Map<URI, Catalog> catalogs = classpathCatalogLoader.getCatalogs();

        for (final URI catalogLocation : catalogs.keySet()) {
            for (final Group group : catalogs.get(catalogLocation).getGroups()) {
                for (final Schema schema : group.getSchemas()) {
                    final String location = schema.getLocation();
                    final Optional<String> baseLocation = ofNullable(group.getBaseLocation());

                    final String schemaId = schema.getId();
                    final URL schemaLocationUrl = schemaResolver.resolve(catalogLocation, location, baseLocation);

                    if (schemaLocations.containsKey(schemaId)) {
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
