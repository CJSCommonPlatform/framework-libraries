package uk.gov.justice.schema.catalog;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;

import java.net.URI;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.inject.Inject;

public class CatalogToSchemaResolver {

    private final ClasspathCatalogLoader classpathCatalogLoader;
    private final SchemaResolver schemaResolver;

    @Inject
    public CatalogToSchemaResolver(final ClasspathCatalogLoader classpathCatalogLoader, final SchemaResolver schemaResolver) {
        this.classpathCatalogLoader = classpathCatalogLoader;
        this.schemaResolver = schemaResolver;
    }

    public Map<String, URL> resolveSchemaLocations() {

        return classpathCatalogLoader.getCatalogs().entrySet().stream()
                .flatMap(catalogEntry -> catalogEntry.getValue().getGroup().stream()
                        .flatMap(group -> group.getSchema().stream()
                                .map(schema -> resolveToUrl(group, schema, catalogEntry.getKey()))))
                .collect(toMap(Entry::getKey, Entry::getValue));
    }

    private Entry<String, URL> resolveToUrl(final Group group, final Schema schema, final URI catalogUri) {
        final String location = schema.getLocation();
        final Optional<String> baseLocation = ofNullable(group.getBaseLocation());
        final URL url = schemaResolver.resolve(catalogUri, location, baseLocation);

        return new SimpleEntry<>(schema.getId(), url);
    }
}
