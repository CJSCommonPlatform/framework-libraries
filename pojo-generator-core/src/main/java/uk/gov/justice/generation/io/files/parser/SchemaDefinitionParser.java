package uk.gov.justice.generation.io.files.parser;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.io.files.loader.InputStreamToJsonObjectConverter;
import uk.gov.justice.generation.io.files.loader.Resource;
import uk.gov.justice.generation.io.files.loader.ResourceLoader;
import uk.gov.justice.generation.io.files.loader.ResourceLoaderFactory;
import uk.gov.justice.generation.io.files.resolver.SchemaCatalogResolver;
import uk.gov.justice.generation.io.files.resolver.SchemaResolver;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.schema.catalog.CatalogObjectFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import org.everit.json.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaDefinitionParser implements FileParser<SchemaDefinition> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SchemaDefinitionParser.class);

    private final CatalogObjectFactory catalogObjectFactory = new CatalogObjectFactory();

    private final SchemaCatalogResolver schemaCatalogResolver = new SchemaCatalogResolver(
            catalogObjectFactory.rawCatalog(),
            catalogObjectFactory.schemaClientFactory(),
            catalogObjectFactory.jsonToSchemaConverter());

    private final InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter = new InputStreamToJsonObjectConverter();
    private final ResourceLoaderFactory resourceLoaderFactory = new ResourceLoaderFactory();
    private final SchemaResolver schemaResolver = new SchemaResolver(schemaCatalogResolver);

    /**
     * Takes a collection of resource paths, loads the resources and parses them into {@link SchemaDefinition}
     * objects.
     *
     * @param basePath the base directory to load the resources from
     * @param paths    the resources to parse
     * @return the {@link SchemaDefinition} models
     */
    public Collection<SchemaDefinition> parse(final Path basePath, final Collection<Path> paths) {
        final ResourceLoader resourceLoader = resourceLoaderFactory.resourceLoaderFor(basePath);

        return paths.stream()
                .map(path -> parse(basePath, path, resourceLoader))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<SchemaDefinition> parse(final Path basePath, final Path resourcePath, final ResourceLoader resourceLoader) {
        try {
            final Resource resource = new Resource(basePath, resourcePath, resourceLoader, inputStreamToJsonObjectConverter);
            final Schema schema = schemaResolver.resolve(resource);
            final String filename = resourcePath.toFile().getName();

            return Optional.of(new SchemaDefinition(filename, schema));
        } catch (final Exception ex) {
            LOGGER.warn(format("Skipping resource for basePath: %s, resourcePath: %s, with reason: %s",
                    basePath,
                    resourcePath,
                    ex.getMessage()));

            return Optional.empty();
        }
    }
}
