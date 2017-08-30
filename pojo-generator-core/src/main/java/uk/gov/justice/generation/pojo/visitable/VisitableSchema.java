package uk.gov.justice.generation.pojo.visitable;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.visitable.acceptor.Acceptable;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorFactory;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.Schema;

public class VisitableSchema implements Visitable {

    private final Schema schema;
    private final AcceptorFactory jsonAcceptorFactory;

    public VisitableSchema(final Schema schema, final AcceptorFactory jsonAcceptorFactory) {
        this.schema = schema;
        this.jsonAcceptorFactory = jsonAcceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor) {
        final Map<Class<? extends Schema>, Acceptable> acceptorMap = jsonAcceptorFactory.acceptorMap();

        if (acceptorMap.containsKey(schema.getClass())) {
            acceptorMap.get(schema.getClass()).accept(fieldName, visitor, schema);
        } else {
            throw new UnsupportedSchemaException(format("Schema of type: %s is not supported.", this.schema.getClass().getSimpleName()));
        }
    }

    public Schema getSchema() {
        return schema;
    }
}
