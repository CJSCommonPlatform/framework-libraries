package uk.gov.justice.generation.pojo.core;

import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerationContext {

    private final Path outputDirectoryPath;
    private final String packageName;
    private final String sourceFilename;
    private final List<String> ignoredClassNames;

    public GenerationContext(
            final Path outputDirectoryPath,
            final String packageName,
            final String sourceFilename,
            final List<String> ignoredClassNames) {
        this.outputDirectoryPath = outputDirectoryPath;
        this.packageName = packageName;
        this.sourceFilename = sourceFilename;
        this.ignoredClassNames = ignoredClassNames;
    }

    public Path getOutputDirectoryPath() {
        return outputDirectoryPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSourceFilename() {
        return sourceFilename;
    }

    public Logger getLoggerFor(final Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public List<String> getIgnoredClassNames() {
        return ignoredClassNames;
    }
}
