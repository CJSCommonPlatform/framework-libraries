package uk.gov.justice.schema.catalog.generation;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

/**
 * Class for running the generation of a json catalog file from within a Maven plugin
 */
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

    /**
     * Generates a json catalog
     * @param catalogName The name of the catalog
     * @param schemaFiles A list of json schema files
     * @param catalogGenerationPath The path to where the catalog should be generated
     * @param jsonSchemaPath A path where json schema files can be found
     */
    public void generateCatalog(final String catalogName, final List<URI> schemaFiles, final Path catalogGenerationPath, final Path jsonSchemaPath) {

        final Catalog catalog = catalogObjectGenerator.generate(
                catalogName,
                asUrls(schemaFiles),
                jsonSchemaPath);

        catalogWriter.write(catalog, catalogGenerationPath);
    }

    private List<URL> asUrls(final List<URI> schemaFiles) {
        return schemaFiles.stream()
                .map(urlConverter::toUrl)
                .collect(toList());
    }
}
