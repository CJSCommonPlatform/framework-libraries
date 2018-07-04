package uk.gov.justice.generation.io.files.resolver;

import uk.gov.justice.generation.io.files.loader.Resource;
import uk.gov.justice.schema.catalog.SchemaCatalogResolver;

import org.everit.json.schema.Schema;
import org.json.JSONObject;

/**
 * Utility class for resolving json schema files from a file or classpath resource
 */
public class SchemaResolver {

    private final SchemaCatalogResolver schemaCatalogResolver;

    public SchemaResolver(final SchemaCatalogResolver schemaCatalogResolver) {
        this.schemaCatalogResolver = schemaCatalogResolver;
    }

    public Schema resolve(final Resource resource) {
        final JSONObject schemaJsonObject = resource.asJsonObject();

        if (schemaJsonObject.has("id")) {
            return schemaCatalogResolver.loadSchema(schemaJsonObject);
        }

        throw new SchemaResolverException("Missing id in Schema. Unable to resolve");
    }
}
