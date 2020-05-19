package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.Schema;

/**
 * The EmptyAcceptor accepts a {@link EmptySchema} and handles visiting the EmptySchema.
 */
public class EmptyAcceptor implements Acceptable {

    /**
     * Accept {@link EmptySchema} and visit schema.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        visitor.visit(fieldName, (EmptySchema) schema);
    }
}
