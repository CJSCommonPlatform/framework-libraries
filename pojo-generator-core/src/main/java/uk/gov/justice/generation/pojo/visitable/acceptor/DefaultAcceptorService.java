package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
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

/**
 * The default implementation of the {@link AcceptorService}, provides the default acceptor map and
 * provides a method to visit a child {@link Schema}.
 */
public class DefaultAcceptorService implements AcceptorService {

    private final Map<Class<? extends Schema>, Acceptable> acceptorMap = new HashMap<>();
    private final VisitableFactory visitableFactory;

    public DefaultAcceptorService(final VisitableFactory visitableFactory) {
        this.visitableFactory = visitableFactory;

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
    public void visitSchema(final String fieldName, final Schema schema, final Visitor visitor) {
        visitableFactory.createWith(fieldName, schema, this).accept(visitor);
    }
}
