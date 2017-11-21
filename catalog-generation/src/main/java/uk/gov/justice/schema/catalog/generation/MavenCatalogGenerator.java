package uk.gov.justice.schema.catalog.generation;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;
import uk.gov.justice.schema.catalog.generation.maven.CatalogGeneratorProperties;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;

public class MavenCatalogGenerator implements Generator<List<URI>> {

    private final ObjectFactory objectFactory;

    public MavenCatalogGenerator() {
        this(new ObjectFactory());
    }

    @VisibleForTesting
    public MavenCatalogGenerator(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public void run(final List<URI> schemaFiles, final GeneratorConfig generatorConfig) {

        final Path catalogGenerationPath = generatorConfig.getOutputDirectory();

        final CatalogGenerationRunner catalogGenerationRunner = objectFactory.catalogGenerationRunner();

        final CatalogGeneratorProperties generatorProperties =
                (CatalogGeneratorProperties) generatorConfig.getGeneratorProperties();
        final String catalogName = generatorProperties.getCatalogName();
        final Path jsonSchemaPath = generatorProperties.getJsonSchemaPath();

        catalogGenerationRunner.generateCatalog(catalogName, schemaFiles, catalogGenerationPath, jsonSchemaPath);
    }
}
