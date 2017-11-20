package uk.gov.justice.schema.catalog;

import static java.lang.String.format;

import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

public class SchemaResolver {

    private static final String AN_EMPTY_STRING = "";

    private final UrlConverter urlConverter;
    private final UriResolver uriResolver;

    @Inject
    public SchemaResolver(final UrlConverter urlConverter, final UriResolver uriResolver) {
        this.urlConverter = urlConverter;
        this.uriResolver = uriResolver;
    }

    public URL resolve(
            final URI catalogUri,
            final String fileLocation,
            final Optional<String> fileBaseLocation) {

        try {
            final URI schemaUri = URI.create(fileBaseLocation.orElse(AN_EMPTY_STRING)).resolve(fileLocation);
            final URI resolvedUri = uriResolver.resolve(catalogUri, schemaUri);

            return urlConverter.toUrl(resolvedUri);

        } catch (final IllegalArgumentException | URISyntaxException e) {
            throw new SchemaCatalogException(format("Failed to resolve %s, %s, %s", catalogUri, fileLocation, fileBaseLocation.orElse(null)), e);
        }
    }
}
