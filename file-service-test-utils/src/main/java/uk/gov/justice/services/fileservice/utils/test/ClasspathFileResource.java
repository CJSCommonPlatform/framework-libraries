package uk.gov.justice.services.fileservice.utils.test;

import static java.lang.String.format;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class ClasspathFileResource {

    public File getFileFromClasspath(final String fileName) {
        try {
            final URL url = getClass().getResource(fileName);
            return new File(url.toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(format("Failed to load file '%s' from classpath", fileName), e);
        }
    }
}
