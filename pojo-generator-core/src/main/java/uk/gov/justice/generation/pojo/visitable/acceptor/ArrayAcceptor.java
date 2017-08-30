package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;

public class ArrayAcceptor implements Acceptable {

    private final AcceptorFactory acceptorFactory;

    public ArrayAcceptor(final AcceptorFactory acceptorFactory) {
        this.acceptorFactory = acceptorFactory;
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
        acceptorFactory.visitSchema(fieldName, visitor, schema);
        visitor.leave(arraySchema);
    }
}
