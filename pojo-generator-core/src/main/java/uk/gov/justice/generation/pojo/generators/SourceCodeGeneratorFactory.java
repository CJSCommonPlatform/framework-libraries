package uk.gov.justice.generation.pojo.generators;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

public class SourceCodeGeneratorFactory {

    public SourceCodeGenerator createFor(final Definition definition) {

        if (definition.getClass() == FieldDefinition.class) {
            return new FieldGenerator((FieldDefinition) definition);
        }

        return new ClassGenerator((ClassDefinition) definition);
    }
}
