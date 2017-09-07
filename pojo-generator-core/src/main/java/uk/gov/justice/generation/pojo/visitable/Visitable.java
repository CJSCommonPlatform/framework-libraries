package uk.gov.justice.generation.pojo.visitable;

import uk.gov.justice.generation.pojo.visitor.Visitor;

/**
 * Defines a visitable class for visiting a schema that can accept a {@link Visitor}
 */
public interface Visitable {

    /**
     * Accept the {@link Visitor} that will visit the schema.
     *
     * @param visitor - the {@link Visitor} visiting this schema
     */
    void accept(final Visitor visitor);
}
