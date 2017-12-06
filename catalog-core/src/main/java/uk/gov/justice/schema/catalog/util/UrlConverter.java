package uk.gov.justice.schema.catalog.util;

import static java.lang.String.format;

import uk.gov.justice.schema.catalog.SchemaCatalogException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class for converting {@link URL}s to {@link URI}s, that handles any exceptions
 * thrown by the conversion.
 */
public class UrlConverter {

    /**
     * Converts a {@link URI} to a {@link URL}
     * @param url A {@link URL} to convert
     * @return The converted {@link URI}
     */
    public URI toUri(final URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new SchemaCatalogException(format("Failed to convert URL '%s' to URI", url), e);
        }
    }

    /**
     * Converts a {@link URI} to a {@link URL}
     * @param uri A {@link URI} to convert
     * @return The converted {@link URL}
     */
    public URL toUrl(final URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new SchemaCatalogException(format("Failed to convert URI '%s' to URL", uri), e);
        }
    }

    /**
     * Converts a {@link String} URI to a {@link URI} Object
     * @param uri A URI in String form
     * @return A {@link URI} Object of that String uri
     */
    public URI toUri(final String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new SchemaCatalogException(format("Failed to convert URI '%s' to URL", uri), e);
        }
    }
}
