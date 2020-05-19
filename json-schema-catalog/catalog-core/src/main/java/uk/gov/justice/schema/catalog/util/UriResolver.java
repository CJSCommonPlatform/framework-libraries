package uk.gov.justice.schema.catalog.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Resolves one {@link URI} against another {@link URI} handling any problems if
 * the {@link URI} is opaque (as is the URI to a resource in a jar file)
 */
public class UriResolver {

    /**
     * Resolve one {@link URI} against another {@link URI} handling any problems if
     * the {@link URI} is opaque (as is the URI to a resource in a jar file)
     *
     * @param baseUri A URI
     * @param otherUri Another URI to resolve
     * @return The resolved URI
     * @throws URISyntaxException If either uri's syntax is malformed
     */
    public URI resolve(final URI baseUri, final URI otherUri) throws URISyntaxException {

        if (baseUri.isOpaque()) {

            final String scheme = baseUri.getScheme();
            final String schemeSpecificPart = baseUri.getSchemeSpecificPart();
            final String fragment = baseUri.getFragment();
            final URI resolvedUri = new URI(schemeSpecificPart).resolve(otherUri);

            return new URI(scheme, resolvedUri.toString(), fragment);
        }

        return baseUri.resolve(otherUri);
    }
}
