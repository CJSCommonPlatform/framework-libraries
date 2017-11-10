package uk.gov.justice.schema.catalog;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonStringToSchemaConverter {

    public Schema convert(final String schemaJson, final SchemaClient schemaClient) {
        return SchemaLoader.builder()
                .schemaJson(new JSONObject(new JSONTokener(schemaJson)))
                .httpClient(schemaClient)
                .build()
                .load()
                .build();
    }
}
