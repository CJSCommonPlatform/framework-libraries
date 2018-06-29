package uk.gov.justice.generation.io.files.parser;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.io.files.loader.Resource;
import uk.gov.justice.generation.io.files.loader.ResourceLoader;
import uk.gov.justice.generation.io.files.loader.ResourceLoaderFactory;
import uk.gov.justice.generation.io.files.resolver.SchemaResolver;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.schema.catalog.CatalogUpdater;
import uk.gov.justice.schema.catalog.RawCatalog;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import org.everit.json.schema.Schema;
import org.slf4j.Logger;

public class SchemaDefinitionParser implements FileParser<SchemaDefinition> {

    private final ResourceLoaderFactory resourceLoaderFactory;
    private final SchemaResolver schemaResolver;
    private final RawCatalog rawCatalog;
    private final ResourceProvider resourceProvider;
    private final Logger logger;

    public SchemaDefinitionParser(
            final ResourceLoaderFactory resourceLoaderFactory,
            final SchemaResolver schemaResolver,
            final RawCatalog rawCatalog,
            final ResourceProvider resourceProvider,
            final Logger logger) {
        this.resourceLoaderFactory = resourceLoaderFactory;
        this.schemaResolver = schemaResolver;
        this.rawCatalog = rawCatalog;
        this.resourceProvider = resourceProvider;
        this.logger = logger;
    }

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

        rawCatalog.updateCatalogSchemaCache(basePath, paths);

        return paths.stream()
                .map(path -> parse(basePath, path, resourceLoader))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<SchemaDefinition> parse(final Path basePath, final Path resourcePath, final ResourceLoader resourceLoader) {
        try {
            final Resource resource = resourceProvider.getResource(basePath, resourcePath, resourceLoader);
            final Schema schema = schemaResolver.resolve(resource);
            final String filename = resourcePath.toFile().getName();

            return Optional.of(new SchemaDefinition(filename, schema));
        } catch (final RuntimeException e) {
            logger.warn(format("Skipping resource for basePath: %s, resourcePath: %s, with reason: %s",
                    basePath,
                    resourcePath,
                    e.getMessage()));
            return Optional.empty();
        }
    }
}
