package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

public class ReferenceAcceptor implements Acceptable {

    private final AcceptorFactory acceptorFactory;

    public ReferenceAcceptor(final AcceptorFactory acceptorFactory) {
        this.acceptorFactory = acceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        final ReferenceSchema referenceSchema = (ReferenceSchema) schema;
        final Schema referredSchema = referenceSchema.getReferredSchema();
        acceptorFactory.visitSchema(fieldName, visitor, referredSchema);
    }
}
