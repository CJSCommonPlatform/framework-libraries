package uk.gov.justice.generation.pojo.visitable;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.visitable.acceptor.Acceptable;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.Schema;

/**
 * A visitable schema that uses {@link Acceptable} implementations for each {@link Schema}
 * implementation type visited.
 */
public class VisitableSchema implements Visitable {

    private final String fieldName;
    private final Schema schema;
    private final AcceptorService jsonAcceptorService;

    /**
     * Construct a VisitableSchema instance with {@link Schema} and {@link AcceptorService}
     *
     * @param fieldName       - the field name of the schema
     * @param schema          - {@link Schema} to be visited
     * @param acceptorService - {@link AcceptorService} creates acceptor map for looking up correct
     *                        {@link Acceptable} implementation
     */
    VisitableSchema(final String fieldName, final Schema schema, final AcceptorService acceptorService) {
        this.fieldName = fieldName;
        this.schema = schema;
        this.jsonAcceptorService = acceptorService;
    }

    @Override
    public void accept(final Visitor visitor) {
        final Map<Class<? extends Schema>, Acceptable> acceptorMap = jsonAcceptorService.acceptorMap();

        if (acceptorMap.containsKey(schema.getClass())) {
            acceptorMap.get(schema.getClass()).accept(fieldName, schema, visitor);
        } else {
            throw new UnsupportedSchemaException(format("Schema of type: %s is not supported.", this.schema.getClass().getSimpleName()));
        }
    }
}
