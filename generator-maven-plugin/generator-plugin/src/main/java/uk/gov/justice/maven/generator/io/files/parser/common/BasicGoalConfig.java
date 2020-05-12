package uk.gov.justice.maven.generator.io.files.parser.common;

import java.nio.file.Path;
import java.util.List;

/**
 * POJO to hold config for maven plugin goal.
 */

public class BasicGoalConfig {

    private final Path sourceDirectory;

    private final List<String> includes;

    private final List<String> excludes;

    public BasicGoalConfig(final Path sourceDirectory,
                           final List<String> includes,
                           final List<String> excludes) {
        this.sourceDirectory = sourceDirectory;
        this.includes = includes;
        this.excludes = excludes;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }
}
