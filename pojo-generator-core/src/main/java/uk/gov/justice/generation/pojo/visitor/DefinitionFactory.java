package uk.gov.justice.generation.pojo.visitor;

import uk.gov.justice.generation.pojo.dom.Definition;

import org.everit.json.schema.Schema;

public interface DefinitionFactory {

    Definition constructRootClassDefinition(final String fieldName);

    Definition constructDefinitionFor(final String fieldName, final Schema schema);
}
