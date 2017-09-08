package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitable.VisitableSchemaFactory;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.HashMap;
import java.util.Map;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class DefaultAcceptorFactory implements AcceptorFactory {

    private final Map<Class<? extends Schema>, Acceptable> acceptorMap = new HashMap<>();
    private final VisitableSchemaFactory visitableSchemaFactory;

    public DefaultAcceptorFactory(final VisitableSchemaFactory visitableSchemaFactory) {
        this.visitableSchemaFactory = visitableSchemaFactory;

        acceptorMap.put(ArraySchema.class, new ArrayAcceptor(this));
        acceptorMap.put(CombinedSchema.class, new CombinedAcceptor(this));
        acceptorMap.put(ObjectSchema.class, new ObjectAcceptor(this));
        acceptorMap.put(ReferenceSchema.class, new ReferenceAcceptor(this));
        acceptorMap.put(StringSchema.class, new StringAcceptor());
        acceptorMap.put(BooleanSchema.class, new BooleanAcceptor());
        acceptorMap.put(NumberSchema.class, new NumberAcceptor());
        acceptorMap.put(EnumSchema.class, new EnumAcceptor());
        acceptorMap.put(NullSchema.class, new NullAcceptor());
        acceptorMap.put(EmptySchema.class, new EmptyAcceptor());
    }

    @Override
    public Map<Class<? extends Schema>, Acceptable> acceptorMap() {
        return acceptorMap;
    }

    @Override
    public void visitSchema(final String fieldName, final Visitor visitor, final Schema schema) {
        visitableSchemaFactory.createWith(schema, this).accept(fieldName, visitor);
    }
}
