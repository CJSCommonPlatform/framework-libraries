package uk.gov.justice.generation.pojo.visitable;

import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;

import org.everit.json.schema.Schema;

/**
 * Factory for constructing a {@link Visitable} instances.
 */
public class VisitableFactory {

    /**
     * Create a {@link VisitableSchema} instance of {@link Visitable}.
     *
     * @param fieldName       - the field name to use in construction
     * @param schema          - the {@link Schema} to use in construction
     * @param acceptorService - the {@link AcceptorService} to use in construction
     * @return a {@link Visitable} instance
     */
    public Visitable createWith(final String fieldName, final Schema schema, final AcceptorService acceptorService) {
        return new VisitableSchema(fieldName, schema, acceptorService);
    }
}
