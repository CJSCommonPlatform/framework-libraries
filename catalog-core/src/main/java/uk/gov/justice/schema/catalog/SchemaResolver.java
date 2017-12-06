package uk.gov.justice.schema.catalog;

import static java.lang.String.format;

import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

/**
 * Resolves the actual URI of a json schema file based on the absolute uri of
 * the catalog file, an {@link Optional} base location relative to the catalog uri
 * and the relative path of the schema file.<br/>
 *
 * <p>
 *  For instance: given a catalog file<br/>
 *  '<b>file:/json/schemas/schema-catalog.json</b>'<br/>
 *
 *  A relative base location of <br/>
 *  <b>'domain/objects'</b><br/>
 *
 *  And a schema location of<br/>
 *  <b>definitions/address.json</b><br/>
 *
 *  This would be resolved into the actual location of the json schema file <br/>
 *  <b>file:/json/schemas/domain/objects/definitions/address.json</b>
 *
 * </p>
 *
 */
public class SchemaResolver {

    private static final String AN_EMPTY_STRING = "";

    private final UrlConverter urlConverter;
    private final UriResolver uriResolver;

    public SchemaResolver(final UrlConverter urlConverter, final UriResolver uriResolver) {
        this.urlConverter = urlConverter;
        this.uriResolver = uriResolver;
    }

    /**
     * Resolves the absolute uri of a json schema relative to a catalog file
     *
     * @param uriOfCatalogFile An absolute URL of a catalog file
     * @param relativeLocationOfSchema A relative path of a Schema file
     * @param schemaBaseLocation An {@link Optional} base location of the schema file
     * @return The absolute uri of a json schema file
     */
    public URL resolve(
            final URI uriOfCatalogFile,
            final String relativeLocationOfSchema,
            final Optional<String> schemaBaseLocation) {

        try {
            final URI schemaUri = new URI(schemaBaseLocation.orElse(AN_EMPTY_STRING)).resolve(relativeLocationOfSchema);
            final URI resolvedUri = uriResolver.resolve(uriOfCatalogFile, schemaUri);

            return urlConverter.toUrl(resolvedUri);

        } catch (final URISyntaxException e) {
            throw new SchemaCatalogException(format("Failed to resolve '%s', to file location '%s', with base location '%s'", uriOfCatalogFile, relativeLocationOfSchema, schemaBaseLocation.orElse(null)), e);
        }
    }
}
