package uk.gov.justice.generation.pojo.generators;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.util.List;

public class JavaGeneratorFactory {

    public ElementGeneratable createGeneratorFor(final Definition definition) {

        if (definition.getClass() == ClassDefinition.class) {
            return new ElementGenerator(definition);
        }

        if (definition.getClass() == EnumDefinition.class) {
            return new StringElementGenerator(definition);
        }

        return new FieldGenerator((FieldDefinition) definition);
    }

    public List<ClassGeneratable> createClassGeneratorsFor(final List<Definition> definitions) {
        return definitions.stream()
                .filter(definition -> EnumDefinition.class.isInstance(definition) || ClassDefinition.class.isInstance(definition))
                .map(definition -> {
                    if (definition.getClass() == EnumDefinition.class) {
                        return new EnumGenerator((EnumDefinition) definition);
                    }

                    return new ClassGenerator((ClassDefinition) definition, this);
                })
                .collect(toList());
    }
}
