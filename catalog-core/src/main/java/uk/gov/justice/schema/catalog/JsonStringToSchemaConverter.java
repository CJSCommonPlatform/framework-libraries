package uk.gov.justice.schema.catalog;

import static java.lang.String.format;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Converts a raw Json Schema into an Everit {@link Schema} resolving any referred Schemas
 * in the raw Json Schema to their location on the classpath
 */
public class JsonStringToSchemaConverter {

    /**
     * Converts a raw Json Schema into an Everit {@link Schema} resolving any referred Schemas
     * in the raw Json Schema to their location on the classpath
     *
     * @param schemaJson The raw Json Schema as a {@link String}
     * @param schemaClient An implementation of Everit's {@link SchemaClient} that
     *                                    rather than returning a schema found at the Schema id,
     *                                    returns a Schema found on the classpath mapped to that
     *                                    Schema id
     * @return A fully resolved Everit {@link Schema}
     */
    public Schema convert(final String schemaJson, final SchemaClient schemaClient) {

        try {
            return SchemaLoader.builder()
                    .schemaJson(new JSONObject(new JSONTokener(schemaJson)))
                    .httpClient(schemaClient)
                    .build()
                    .load()
                    .build();
        } catch (final JSONException e) {
            throw new SchemaCatalogException(format("Failed to convert schema json to Schema Object. Schema json:%n%s", schemaJson), e);
        }
    }
}
