package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;

public class ObjectAcceptor implements Acceptable {

    private final AcceptorFactory acceptorFactory;

    public ObjectAcceptor(final AcceptorFactory acceptorFactory) {
        this.acceptorFactory = acceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        final ObjectSchema objectSchema = (ObjectSchema) schema;

        final Map<String, Schema> propertySchemas = objectSchema.getPropertySchemas();

        visitor.enter(fieldName, objectSchema);
        propertySchemas.forEach((childName, childSchema) -> acceptorFactory.visitSchema(childName, visitor, childSchema));
        visitor.leave(objectSchema);
    }
}
