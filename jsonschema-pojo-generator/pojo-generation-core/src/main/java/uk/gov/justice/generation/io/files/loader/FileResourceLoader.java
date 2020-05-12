package uk.gov.justice.generation.io.files.loader;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Load a resource from a file
 */
public class FileResourceLoader implements ResourceLoader {

    @Override
    public InputStream loadFrom(final Path basePath, final Path resourcePath) {
        final File file = basePath.resolve(resourcePath).toAbsolutePath().toFile();

        try {
            return new FileInputStream(file);
        } catch (final FileNotFoundException ex) {
            throw new ResourceNotFoundException(format("Unable to load file: %s", file.toString()), ex);
        }
    }
}
