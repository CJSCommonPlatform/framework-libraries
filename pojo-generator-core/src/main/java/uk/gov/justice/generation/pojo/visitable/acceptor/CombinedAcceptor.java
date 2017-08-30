package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Collection;

import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.Schema;

public class CombinedAcceptor implements Acceptable {

    private final AcceptorFactory acceptorFactory;

    public CombinedAcceptor(final AcceptorFactory acceptorFactory) {
        this.acceptorFactory = acceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        final CombinedSchema combinedSchema = (CombinedSchema) schema;

        final Collection<Schema> subschemas = combinedSchema.getSubschemas();

        visitor.enter(fieldName, combinedSchema);
        subschemas.forEach(childSchema -> acceptorFactory.visitSchema(fieldName, visitor, childSchema));
        visitor.leave(combinedSchema);
    }
}
