package uk.gov.justice.generation.pojo.core;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerationContext {

    private final Path outputDirectoryPath;

    public GenerationContext(final Path outputDirectoryPath) {
        this.outputDirectoryPath = outputDirectoryPath;
    }

    public Path getOutputDirectoryPath() {
        return outputDirectoryPath;
    }

    public Logger getLoggerFor(final Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
