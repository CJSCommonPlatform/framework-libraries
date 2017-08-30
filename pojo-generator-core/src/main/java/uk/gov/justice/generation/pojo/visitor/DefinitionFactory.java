package uk.gov.justice.generation.pojo.visitor;

import uk.gov.justice.generation.pojo.dom.Definition;

import org.everit.json.schema.Schema;

public interface DefinitionFactory {

    Definition constructDefinitionWithEventFor(String fieldName, String packageName, Schema schema);

    Definition constructDefinitionFor(String fieldName, String packageName, Schema schema);

    Definition constructFieldDefinition(String fieldName, Schema schema);
}
