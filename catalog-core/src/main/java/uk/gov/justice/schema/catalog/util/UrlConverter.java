package uk.gov.justice.schema.catalog.util;

import static java.lang.String.format;

import uk.gov.justice.schema.catalog.SchemaCatalogException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlConverter {

    public URI toUri(final URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new SchemaCatalogException(format("Failed to convert URL '%s' to URI", url), e);
        }
    }

    public URL toUrl(final URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new SchemaCatalogException(format("Failed to convert URI '%s' to URL", uri), e);
        }
    }

    public URI toUri(final String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new SchemaCatalogException(format("Failed to convert URI '%s' to URL", uri), e);
        }
    }
}
