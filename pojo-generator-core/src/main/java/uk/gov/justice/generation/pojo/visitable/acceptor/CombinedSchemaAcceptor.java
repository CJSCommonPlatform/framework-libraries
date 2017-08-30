package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Collection;

import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.Schema;

public class CombinedSchemaAcceptor implements JsonSchemaAcceptor {

    private final JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    public CombinedSchemaAcceptor(final JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory) {
        this.jsonSchemaAcceptorFactory = jsonSchemaAcceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        final CombinedSchema combinedSchema = (CombinedSchema) schema;

        final Collection<Schema> subschemas = combinedSchema.getSubschemas();

        visitor.enter(fieldName, combinedSchema);
        subschemas.forEach(childSchema -> jsonSchemaAcceptorFactory.visitSchema(fieldName, visitor, childSchema));
        visitor.leave(combinedSchema);
    }
}
