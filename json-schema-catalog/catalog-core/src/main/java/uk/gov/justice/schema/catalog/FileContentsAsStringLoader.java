package uk.gov.justice.schema.catalog;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 *  Gets the contents of a file as a {@link String}
 */
public class FileContentsAsStringLoader {

    /**
     * Gets the contents of a file as a {@link String}
     *
     * @param urlOfFile The {@link java.net.URI} of the file
     *
     * @return The File's contents as a UTF 8 String.
     */
    public String readFileContents(final URL urlOfFile) {

        try  {
            return IOUtils.toString(urlOfFile, UTF_8);
        } catch (final IOException e) {
            throw new SchemaCatalogException(format("Failed to read file contents from '%s'", urlOfFile), e);
        }
    }
}
