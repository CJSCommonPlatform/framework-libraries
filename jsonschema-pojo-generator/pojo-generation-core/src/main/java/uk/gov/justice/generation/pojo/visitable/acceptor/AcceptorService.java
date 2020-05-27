package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.Schema;

/**
 * Defines an AcceptorService that creates a Map of {@link Schema} class types as the key and an
 * {@link Acceptable} class type that knows how to visit the given Schema type.
 *
 * The AcceptorService also provides a method to allow the visiting of child Schemas.
 */
public interface AcceptorService {

    /**
     * Provide a {@link Map} of {@link Schema} class types as the key and an {@link Acceptable}
     * class type that knows how to visit the given Schema type.
     *
     * @return the acceptorMap
     */
    Map<Class<? extends Schema>, Acceptable> acceptorMap();

    /**
     * Visits the given child {@link Schema}
     *
     * @param fieldName   - the field name of the child schema
     * @param childSchema - the {@link Schema} to visit
     * @param visitor     - the {@link Visitor} to visit the schema
     */
    void visitSchema(final String fieldName, final Schema childSchema, final Visitor visitor);
}
