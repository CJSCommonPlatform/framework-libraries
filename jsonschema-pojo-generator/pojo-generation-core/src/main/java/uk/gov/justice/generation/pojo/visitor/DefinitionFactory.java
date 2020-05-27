package uk.gov.justice.generation.pojo.visitor;

import uk.gov.justice.generation.pojo.dom.Definition;

import org.everit.json.schema.Schema;

/**
 * The DefinitionFactory defines an interface for converting a given field name and {@link Schema}
 * into a {@link Definition}.
 */
public interface DefinitionFactory {

    /**
     * Constructs a ROOT {@link Definition} for the given field name
     *
     * @param fieldName the field name to use for the ROOT definition
     * @return the {@link Definition}
     */
    Definition constructRootDefinitionFor(final String fieldName, final Schema schema);

    /**
     * Constructs a {@link Definition} for the given field name and {@link Schema}.
     *
     * @param fieldName the field name to use for the definition
     * @param schema    the {@link Schema} to use to decide the Definition type to return
     * @return the {@link Definition}
     */
    Definition constructDefinitionFor(final String fieldName, final Schema schema);
}
