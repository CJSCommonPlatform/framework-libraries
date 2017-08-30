package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitable.JsonSchemaWrapperFactory;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.HashMap;
import java.util.Map;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class DefaultJsonSchemaAcceptorFactory implements JsonSchemaAcceptorFactory {

    private final Map<Class<? extends Schema>, JsonSchemaAcceptor> acceptorMap = new HashMap<>();
    private final JsonSchemaWrapperFactory jsonSchemaWrapperFactory;

    public DefaultJsonSchemaAcceptorFactory(final JsonSchemaWrapperFactory jsonSchemaWrapperFactory) {
        this.jsonSchemaWrapperFactory = jsonSchemaWrapperFactory;

        acceptorMap.put(ArraySchema.class, new ArraySchemaAcceptor(this));
        acceptorMap.put(CombinedSchema.class, new CombinedSchemaAcceptor(this));
        acceptorMap.put(ObjectSchema.class, new ObjectSchemaAcceptor(this));
        acceptorMap.put(ReferenceSchema.class, new ReferenceSchemaAcceptor(this));
        acceptorMap.put(StringSchema.class, new StringSchemaAcceptor());
        acceptorMap.put(BooleanSchema.class, new BooleanSchemaAcceptor());
        acceptorMap.put(NumberSchema.class, new NumberSchemaAcceptor());
        acceptorMap.put(EnumSchema.class, new EnumSchemaAcceptor());
    }

    @Override
    public Map<Class<? extends Schema>, JsonSchemaAcceptor> acceptorMap() {
        return acceptorMap;
    }

    @Override
    public void visitSchema(final String fieldName, final Visitor visitor, final Schema schema) {
        jsonSchemaWrapperFactory.createWith(schema, this).accept(fieldName, visitor);
    }
}
