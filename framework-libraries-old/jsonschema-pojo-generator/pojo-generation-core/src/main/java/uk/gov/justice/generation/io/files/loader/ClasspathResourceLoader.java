package uk.gov.justice.generation.io.files.loader;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Load a resource from the classpath
 */
public class ClasspathResourceLoader implements ResourceLoader {

    @Override
    public InputStream loadFrom(final Path basePath, final Path resourcePath) {
        final String classpathResource = convertPathToClasspathResource(resourcePath);

        return ofNullable(loadResourceWithLocalClassLoader(classpathResource)
                .orElse(loadResourceWithThreadClassLoader(classpathResource)))
                .orElseThrow(() -> new ResourceNotFoundException(format("Unable to load resource: %s", classpathResource)));
    }

    private Optional<InputStream> loadResourceWithLocalClassLoader(final String resourcePath) {
        return ofNullable(
                getClass()
                        .getClassLoader()
                        .getResourceAsStream(resourcePath));
    }

    private InputStream loadResourceWithThreadClassLoader(final String resourcePath) {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
    }

    private String convertPathToClasspathResource(final Path filePath) {
        return filePath.toString().replace("\\", "/");
    }
}
