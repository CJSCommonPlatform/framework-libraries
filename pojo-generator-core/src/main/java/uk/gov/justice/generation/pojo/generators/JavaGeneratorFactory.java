package uk.gov.justice.generation.pojo.generators;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;

import java.util.List;

public class JavaGeneratorFactory {

    private final ClassNameFactory classNameFactory;

    public JavaGeneratorFactory(final ClassNameFactory classNameFactory) {
        this.classNameFactory = classNameFactory;
    }

    public ElementGeneratable createGeneratorFor(final Definition definition) {

        if (definition.getClass() == ClassDefinition.class || definition.getClass() == EnumDefinition.class) {
            return new ElementGenerator(definition, classNameFactory);
        }

        return new FieldGenerator((FieldDefinition) definition, classNameFactory);
    }

    public List<ClassGeneratable> createClassGeneratorsFor(final List<Definition> definitions, final PluginProvider pluginProvider) {
        return definitions.stream()
                .filter(definition -> EnumDefinition.class.isInstance(definition) || ClassDefinition.class.isInstance(definition))
                .map(definition -> {
                    if (definition.getClass() == EnumDefinition.class) {
                        return new EnumGenerator((EnumDefinition) definition);
                    }

                    return new ClassGenerator((ClassDefinition) definition, this, pluginProvider, classNameFactory);
                })
                .collect(toList());
    }
}
