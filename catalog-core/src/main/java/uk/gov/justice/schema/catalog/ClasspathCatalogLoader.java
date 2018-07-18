package uk.gov.justice.schema.catalog;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Loads all {@link Catalog}s from any part of the classpath, which have the path
 * '/json/schema/schema_catalog.json'
 */
public class ClasspathCatalogLoader {

    private final ObjectMapper objectMapper;
    private final ClasspathResourceLoader classpathResourceLoader;
    private final CatalogContext catalogContext;
    private final UrlConverter urlConverter;

    public ClasspathCatalogLoader(final ObjectMapper objectMapper,
                                  final ClasspathResourceLoader classpathResourceLoader,
                                  final CatalogContext catalogContext,
                                  final UrlConverter urlConverter) {
        this.objectMapper = objectMapper;
        this.classpathResourceLoader = classpathResourceLoader;
        this.catalogContext = catalogContext;
        this.urlConverter = urlConverter;
    }

    /**
     * Loads all {@link Catalog}s from any part of the classpath found on the path
     * 'json/schema/schema_catalog.json'
     *
     * @return All found {@link Catalog}s mapped by their URIs
     */
    public Map<URI, Catalog> getCatalogs() {
        return listAllCatalogsFromClasspath().stream()
                .collect(toMap(urlConverter::toUri, this::loadCatalog));
    }

    private Catalog loadCatalog(final URL catalogUrl) {
        try {
            return objectMapper.readValue(catalogUrl, Catalog.class);
        } catch (IOException e) {
            throw new SchemaCatalogException(format("Failed to convert to json loaded from '%s' to a Catalog pojo", catalogUrl.toString()), e);
        }
    }

    private List<URL> listAllCatalogsFromClasspath() {
        final String catalogFullPath = catalogContext.getCatalogFullPath();
        try {
            return classpathResourceLoader.getResources(catalogFullPath);
        } catch (final IOException e) {
            throw new SchemaCatalogException(format("Failed to load the catalogs from the classpath for location '%s'", catalogFullPath), e);
        }
    }
}
