package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.NullSchema;
import org.everit.json.schema.Schema;

/**
 * The NullAcceptor accepts a {@link NullSchema} and handles visiting the NullSchema.
 */
public class NullAcceptor implements Acceptable {

    /**
     * Accept {@link NullSchema} and visit schema.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        visitor.visit(fieldName, (NullSchema) schema);
    }
}
