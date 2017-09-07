package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

/**
 * The ReferenceAcceptor accepts a {@link ReferenceSchema} and handles visiting the
 * ReferenceSchema.
 */
public class ReferenceAcceptor implements Acceptable {

    private final AcceptorService acceptorService;

    public ReferenceAcceptor(final AcceptorService acceptorService) {
        this.acceptorService = acceptorService;
    }

    /**
     * Accept {@link ReferenceSchema}, visit schema and visit the referred schema using the {@link
     * AcceptorService}.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        final ReferenceSchema referenceSchema = (ReferenceSchema) schema;

        visitor.enter(fieldName, referenceSchema);
        acceptorService.visitSchema(fieldName, referenceSchema.getReferredSchema(), visitor);
        visitor.leave(referenceSchema);
    }
}
