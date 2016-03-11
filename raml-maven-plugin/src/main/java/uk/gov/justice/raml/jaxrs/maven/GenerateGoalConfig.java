package uk.gov.justice.raml.jaxrs.maven;

import java.nio.file.Path;
import java.util.List;

/**
 * POJO to hold config for the generate goal.
 */
public class GenerateGoalConfig {

    private final String generatorName;

    private final Path outputDirectory;

    private final Path sourceDirectory;

    private final String basePackageName;

    private final List<String> includes;

    private final List<String> excludes;

    public GenerateGoalConfig(final String generatorName,
                              final Path sourceDirectory,
                              final Path outputDirectory,
                              final String basePackageName,
                              final  List<String> includes,
                              final  List<String> excludes) {
        this.generatorName = generatorName;
        this.outputDirectory = outputDirectory;
        this.sourceDirectory = sourceDirectory;
        this.basePackageName = basePackageName;
        this.includes = includes;
        this.excludes = excludes;
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

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }
}
