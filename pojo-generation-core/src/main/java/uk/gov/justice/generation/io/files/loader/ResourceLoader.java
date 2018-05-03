package uk.gov.justice.generation.io.files.loader;

import java.io.InputStream;
import java.nio.file.Path;

public interface ResourceLoader {

    /**
     * Load a resource from the base path and resource path
     *
     * @param basePath     - the base path to resolve the resource
     * @param resourcePath - the path to the resource
     * @return the resource as an {@link InputStream}
     */
    InputStream loadFrom(final Path basePath, final Path resourcePath);
}
