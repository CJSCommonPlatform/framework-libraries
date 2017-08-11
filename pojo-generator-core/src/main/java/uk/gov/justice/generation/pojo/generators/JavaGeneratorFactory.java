package uk.gov.justice.generation.pojo.generators;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

public class JavaGeneratorFactory {

    public ElementGeneratable createGeneratorFor(final Definition definition) {

        if (definition.getClass() == FieldDefinition.class) {
            return new FieldGenerator((FieldDefinition) definition);
        }

        return new ElementGenerator((ClassDefinition) definition);
    }

    public ClassGeneratable createClassGeneratorFor(final ClassDefinition classDefinition) {
        return new ClassGenerator(classDefinition, this);
    }
}
