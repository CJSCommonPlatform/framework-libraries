package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.Schema;

/**
 * The NumberAcceptor accepts a {@link NumberSchema} and handles visiting the NumberSchema.
 */
public class NumberAcceptor implements Acceptable {

    /**
     * Accept {@link NumberSchema} and visit schema.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        visitor.visit(fieldName, (NumberSchema) schema);
    }
}
