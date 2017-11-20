package uk.gov.justice.schema.catalog.generation;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class CatalogGenerationRunner {

    private final CatalogObjectGenerator catalogObjectGenerator;
    private final CatalogWriter catalogWriter;
    private final UrlConverter urlConverter;

    public CatalogGenerationRunner(
            final CatalogObjectGenerator catalogObjectGenerator,
            final CatalogWriter catalogWriter,
            final UrlConverter urlConverter) {
        this.catalogObjectGenerator = catalogObjectGenerator;
        this.catalogWriter = catalogWriter;
        this.urlConverter = urlConverter;
    }

    public void generateCatalog(final String catalogName, final List<URI> schemaFiles) {

        final Catalog catalog = catalogObjectGenerator.generate(
                catalogName,
                asUrls(schemaFiles));

        catalogWriter.write(catalog);
    }

    private List<URL> asUrls(final List<URI> schemaFiles) {
        return schemaFiles.stream()
                .map(urlConverter::toUrl)
                .collect(toList());
    }
}
