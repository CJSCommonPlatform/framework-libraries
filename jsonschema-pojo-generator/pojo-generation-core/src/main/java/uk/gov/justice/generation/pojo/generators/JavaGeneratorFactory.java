package uk.gov.justice.generation.pojo.generators;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.OptionalTypeNameUtil;

import java.util.List;

/**
 * Factory for creating the correct generator for the specified {@link Definition}
 */
public class JavaGeneratorFactory {

    private final ClassNameFactory classNameFactory;
    private final OptionalTypeNameUtil optionalTypeNameUtil;

    public JavaGeneratorFactory(final ClassNameFactory classNameFactory, final OptionalTypeNameUtil optionalTypeNameUtil) {
        this.classNameFactory = classNameFactory;
        this.optionalTypeNameUtil = optionalTypeNameUtil;
    }

    public ElementGeneratable createGeneratorFor(final Definition definition, final PluginContext pluginContext) {

        if (definition.getClass() == ClassDefinition.class || definition.getClass() == EnumDefinition.class) {
            return new ElementGenerator(definition, classNameFactory, pluginContext, optionalTypeNameUtil);
        }

        return new FieldGenerator(
                (FieldDefinition) definition,
                classNameFactory,
                pluginContext,
                optionalTypeNameUtil);
    }

    public List<ClassGeneratable> createClassGeneratorsFor(final List<Definition> definitions,
                                                           final PluginProvider pluginProvider,
                                                           final PluginContext pluginContext,
                                                           final GenerationContext generationContext) {
        return definitions.stream()
                .filter(this::isClassOrEnum)
                .filter(definition -> isNotHardCoded(definition, generationContext.getIgnoredClassNames()))
                .map(definition -> getClassGeneratable(pluginProvider, pluginContext, definition))
                .collect(toList());
    }

    private boolean isClassOrEnum(final Definition definition) {
        return EnumDefinition.class.isInstance(definition) || ClassDefinition.class.isInstance(definition);
    }

    private boolean isNotHardCoded(final Definition definition, final List<String> hardCodedClassNames) {
        final String className = capitalize(definition.getFieldName());
        return !hardCodedClassNames.contains(className);
    }

    private ClassGeneratable getClassGeneratable(
            final PluginProvider pluginProvider,
            final PluginContext pluginContext,
            final Definition definition) {

        if (definition.getClass() == EnumDefinition.class) {
            return new EnumGenerator((EnumDefinition) definition,
                    classNameFactory,
                    pluginContext);
        }

        return new ClassGenerator((ClassDefinition) definition,
                classNameFactory,
                pluginProvider,
                pluginContext);
    }
}
