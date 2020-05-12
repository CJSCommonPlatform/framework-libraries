package uk.gov.justice.generation.io.files.loader;

import java.nio.file.Path;

/**
 * Creates a {@link ResourceLoader} that either loads from the classpath or the file system.
 */
public class ResourceLoaderFactory {

    private static final String CLASSPATH = "CLASSPATH";

    public ResourceLoader resourceLoaderFor(final Path basePath) {
        if (basePath.endsWith(CLASSPATH)) {
            return new ClasspathResourceLoader();
        }

        return new FileResourceLoader();
    }
}
