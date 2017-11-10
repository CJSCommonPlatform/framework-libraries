package uk.gov.justice.schema.catalog;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URL;
import java.util.Optional;

public class SchemaResolver {

    private static final String AN_EMPTY_STRING = "";

    private final UrlConverter urlConverter;

    public SchemaResolver(final UrlConverter urlConverter) {
        this.urlConverter = urlConverter;
    }

    public URL resolve(
            final URL catalogUrl,
            final String fileLocation,
            final Optional<String> fileBaseLocation) {

        final URI schemaUri = URI.create(fileBaseLocation.orElse(AN_EMPTY_STRING)).resolve(fileLocation);
        final URI uri = urlConverter.toUri(catalogUrl).resolve(schemaUri);

        return urlConverter.toUrl(uri);
    }
}
