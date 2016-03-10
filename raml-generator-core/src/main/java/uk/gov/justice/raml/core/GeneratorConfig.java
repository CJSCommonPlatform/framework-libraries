package uk.gov.justice.raml.core;

import java.nio.file.Path;

public class GeneratorConfig {

    private Path outputDirectory;
    private String basePackageName;
    private Path sourceDirectory;

    public GeneratorConfig(final Path sourceDirectory, final Path outputDirectory, final String basePackageName) {
        this.outputDirectory = outputDirectory;
        this.basePackageName = basePackageName;
        this.sourceDirectory = sourceDirectory;
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
}
