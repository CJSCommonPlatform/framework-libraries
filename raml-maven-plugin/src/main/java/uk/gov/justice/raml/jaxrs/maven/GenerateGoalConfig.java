package uk.gov.justice.raml.jaxrs.maven;

import java.nio.file.Path;

/**
 * POJO to hold config for the generate goal.
 */
public class GenerateGoalConfig {

    private final String generatorName;

    private final Path outputDirectory;

    private final Path sourceDirectory;

    private final String basePackageName;

    public GenerateGoalConfig(final String generatorName, final Path sourceDirectory, final Path outputDirectory, final String basePackageName) {
        this.generatorName = generatorName;
        this.outputDirectory = outputDirectory;
        this.sourceDirectory = sourceDirectory;
        this.basePackageName = basePackageName;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public String getBasePackageName() {
        return basePackageName;
    }
}
