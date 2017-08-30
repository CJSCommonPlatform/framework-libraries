package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

public class ReferenceSchemaAcceptor implements JsonSchemaAcceptor {

    private final JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    public ReferenceSchemaAcceptor(final JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory) {
        this.jsonSchemaAcceptorFactory = jsonSchemaAcceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        final ReferenceSchema referenceSchema = (ReferenceSchema) schema;
        final Schema referredSchema = referenceSchema.getReferredSchema();
        jsonSchemaAcceptorFactory.visitSchema(fieldName, visitor, referredSchema);
    }
}
