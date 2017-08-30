package uk.gov.justice.generation.pojo.visitable;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.visitable.acceptor.JsonSchemaAcceptor;
import uk.gov.justice.generation.pojo.visitable.acceptor.JsonSchemaAcceptorFactory;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.Schema;

public class JsonSchemaWrapper implements Visitable {

    private final Schema schema;
    private final JsonSchemaAcceptorFactory jsonAcceptorFactory;

    public JsonSchemaWrapper(final Schema schema, final JsonSchemaAcceptorFactory jsonAcceptorFactory) {
        this.schema = schema;
        this.jsonAcceptorFactory = jsonAcceptorFactory;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor) {
        final Map<Class<? extends Schema>, JsonSchemaAcceptor> acceptorMap = jsonAcceptorFactory.acceptorMap();

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
