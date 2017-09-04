package uk.gov.justice.generation.pojo.core;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerationContext {

    private final Path outputDirectoryPath;
    private final String packageName;

    public GenerationContext(final Path outputDirectoryPath, final String packageName) {
        this.outputDirectoryPath = outputDirectoryPath;
        this.packageName = packageName;
    }

    public Path getOutputDirectoryPath() {
        return outputDirectoryPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public Logger getLoggerFor(final Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
