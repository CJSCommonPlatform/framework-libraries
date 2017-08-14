package uk.gov.justice.maven.generator.io.files.parser.generator;

import uk.gov.justice.maven.generator.io.files.parser.common.BasicGoalConfig;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * POJO to hold config for the generate goal.
 */
public class GenerateGoalConfig extends BasicGoalConfig {

    private final String generatorName;

    private final Path outputDirectory;

    private final String basePackageName;

    private final Map<String, String> properties;

    private final List<Path> sourcePaths;

    public GenerateGoalConfig(final String generatorName,
                              final Path sourceDirectory,
                              final Path outputDirectory,
                              final String basePackageName,
                              final List<String> includes,
                              final List<String> excludes,
                              final Map<String, String> properties,
                              final List<Path> sourcePaths) {
        super(sourceDirectory, includes, excludes);
        this.generatorName = generatorName;
        this.outputDirectory = outputDirectory;
        this.basePackageName = basePackageName;
        this.properties = properties;
        this.sourcePaths = sourcePaths;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public List<Path> getSourcePaths() {
        return sourcePaths;
    }
}
