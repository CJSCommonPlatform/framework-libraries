package uk.gov.justice.generation.pojo.core;

import org.everit.json.schema.Schema;

public class JsonSchemaWrapperFactory {

    public JsonSchemaWrapper create(final Schema schema) {
        return new JsonSchemaWrapper(schema);
    }
}
