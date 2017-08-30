package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;

public class ArraySchemaAcceptor implements JsonSchemaAcceptor {

    private final JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    public ArraySchemaAcceptor(final JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory) {
        this.jsonSchemaAcceptorFactory = jsonSchemaAcceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        final ArraySchema arraySchema = (ArraySchema) schema;

        final Schema allItemSchema = arraySchema.getAllItemSchema();

        if (allItemSchema != null) {
            acceptArraySchema(fieldName, arraySchema, allItemSchema, visitor);
        } else {
            arraySchema
                    .getItemSchemas()
                    .forEach(itemSchema -> acceptArraySchema(fieldName, arraySchema, itemSchema, visitor));
        }
    }

    private void acceptArraySchema(final String fieldName, final ArraySchema arraySchema, final Schema schema, final Visitor visitor) {
        visitor.enter(fieldName, arraySchema);
        jsonSchemaAcceptorFactory.visitSchema(fieldName, visitor, schema);
        visitor.leave(arraySchema);
    }
}
