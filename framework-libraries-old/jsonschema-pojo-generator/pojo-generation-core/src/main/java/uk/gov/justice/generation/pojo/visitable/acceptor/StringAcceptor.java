package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

/**
 * The StringAcceptor accepts a {@link StringSchema} and handles visiting the StringSchema.
 */
public class StringAcceptor implements Acceptable {

    /**
     * Accept {@link StringSchema} and visit schema.
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    @Override
    public void accept(final String fieldName, final Schema schema, final Visitor visitor) {
        visitor.visit(fieldName, (StringSchema) schema);
    }
}
