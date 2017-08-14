package uk.gov.justice.generation.pojo.core;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerationContext {

    private final File sourceRootDirectory;

    public GenerationContext(final File sourceRootDirectory) {
        this.sourceRootDirectory = sourceRootDirectory;
    }

    public File getSourceRootDirectory() {
        return sourceRootDirectory;
    }

    public Logger getLoggerFor(final Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
