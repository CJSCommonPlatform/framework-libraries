package uk.gov.justice.schema.catalog;

import static java.lang.String.format;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonStringToSchemaConverter {

    // TODO: test me!!!
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
