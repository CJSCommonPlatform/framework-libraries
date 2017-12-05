package uk.gov.justice.schema.catalog.generation.maven;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;
import uk.gov.justice.schema.catalog.generation.CatalogGenerationRunner;
import uk.gov.justice.schema.catalog.generation.GenerationObjectFactory;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class MavenCatalogGenerator implements Generator<List<URI>> {

    private final GenerationObjectFactory generationObjectFactory;

    public MavenCatalogGenerator(final GenerationObjectFactory generationObjectFactory) {
        this.generationObjectFactory = generationObjectFactory;
    }

    @Override
    public void run(final List<URI> schemaFiles, final GeneratorConfig generatorConfig) {

        final Path catalogGenerationPath = generatorConfig.getOutputDirectory();

        final CatalogGenerationRunner catalogGenerationRunner = generationObjectFactory.catalogGenerationRunner();

        final CatalogGeneratorProperties generatorProperties =
                (CatalogGeneratorProperties) generatorConfig.getGeneratorProperties();
        final String catalogName = generatorProperties.getCatalogName();
        final Path jsonSchemaPath = generatorProperties.getJsonSchemaPath();

        catalogGenerationRunner.generateCatalog(catalogName, schemaFiles, catalogGenerationPath, jsonSchemaPath);
    }
}
