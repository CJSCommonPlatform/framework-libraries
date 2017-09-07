package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Collection;

import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.Schema;

/**
 * The CombinedAcceptor accepts a {@link CombinedSchema} and handles visiting the CombinedSchema.
 */
public class CombinedAcceptor implements Acceptable {

    private final AcceptorService acceptorService;

    public CombinedAcceptor(final AcceptorService acceptorService) {
        this.acceptorService = acceptorService;
    }

    /**
     * Accept {@link CombinedSchema}, visit schema and visit all sub schemas using the {@link
     * AcceptorService}.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        final CombinedSchema combinedSchema = (CombinedSchema) schema;

        final Collection<Schema> subschemas = combinedSchema.getSubschemas();

        visitor.enter(fieldName, combinedSchema);
        subschemas.forEach(childSchema -> acceptorService.visitSchema(fieldName, childSchema, visitor));
        visitor.leave(combinedSchema);
    }
}
