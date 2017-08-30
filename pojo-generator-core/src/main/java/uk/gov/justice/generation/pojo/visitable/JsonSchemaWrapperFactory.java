package uk.gov.justice.generation.pojo.visitable;

import uk.gov.justice.generation.pojo.visitable.acceptor.JsonSchemaAcceptorFactory;

import org.everit.json.schema.Schema;

public class JsonSchemaWrapperFactory {

    public JsonSchemaWrapper createWith(final Schema schema, final JsonSchemaAcceptorFactory schemaAcceptorFactory) {
        return new JsonSchemaWrapper(schema, schemaAcceptorFactory);
    }
}
