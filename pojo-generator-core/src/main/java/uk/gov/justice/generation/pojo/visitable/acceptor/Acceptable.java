package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.Schema;

/**
 * Interface defining an Acceptable that accepts a specific type of {@link Schema}
 */
public interface Acceptable {

    /**
     * Accepts the fieldName, {@link Schema} and {@link Visitor} and handles visiting a specific
     * type of Schema implementation
     *
     * @param fieldName - the field name of the schema being visited
     * @param schema    - the {@link Schema} being visited
     * @param visitor   - the {@link Visitor} to visit the schema
     */
    void accept(final String fieldName, final Schema schema, final Visitor visitor);
}
