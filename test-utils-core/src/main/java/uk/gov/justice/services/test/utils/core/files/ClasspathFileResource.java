package uk.gov.justice.services.test.utils.core.files;

import static java.lang.String.format;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class for loading a classpath resource as a {@link File}
 */
public class ClasspathFileResource {

    /**
     * Loads a resource specified by the supplied path from the classpath.
     *
     * The file path should be from the root of the classpath and should start with a '/'
     *
     * @param filePath the path on the classpath to your file
     * @return the specified resource as a {@link File}
     */
    public File getFileFromClasspath(final String filePath) {
        try {
            final URL url = getClass().getResource(filePath);
            if (url == null) {
                throw new RuntimeException(format("'%s' not found on classpath", filePath));
            }
            return new File(url.toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(format("Failed to load file '%s' from classpath", filePath), e);
        }
    }
}
