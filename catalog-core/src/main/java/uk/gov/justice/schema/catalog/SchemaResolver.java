package uk.gov.justice.schema.catalog;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

public class SchemaResolver {

    private static final String AN_EMPTY_STRING = "";

    private final UrlConverter urlConverter;

    @Inject
    public SchemaResolver(final UrlConverter urlConverter) {
        this.urlConverter = urlConverter;
    }

    public URL resolve(
            final URI catalogUri,
            final String fileLocation,
            final Optional<String> fileBaseLocation) {

        final URI schemaUri = URI.create(fileBaseLocation.orElse(AN_EMPTY_STRING)).resolve(fileLocation);
        return urlConverter.toUrl(catalogUri.resolve(schemaUri));
    }
}
