package uk.gov.justice.schema.catalog.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UriResolver {

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
