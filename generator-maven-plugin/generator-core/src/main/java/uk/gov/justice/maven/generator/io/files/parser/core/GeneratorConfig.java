package uk.gov.justice.maven.generator.io.files.parser.core;

import java.nio.file.Path;
import java.util.List;

public class GeneratorConfig {

    private final Path outputDirectory;
    private final String basePackageName;
    private final Path sourceDirectory;
    private final GeneratorProperties generatorProperties;
    private final List<Path> sourcePaths;

    public GeneratorConfig(final Path sourceDirectory,
                           final Path outputDirectory,
                           final String basePackageName,
                           final GeneratorProperties generatorProperties,
                           final List<Path> sourcePaths) {
        this.outputDirectory = outputDirectory;
        this.basePackageName = basePackageName;
        this.sourceDirectory = sourceDirectory;
        this.generatorProperties = generatorProperties;
        this.sourcePaths = sourcePaths;
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public GeneratorProperties getGeneratorProperties() {
        return generatorProperties;
    }

    public List<Path> getSourcePaths() {
        return sourcePaths;
    }
}
