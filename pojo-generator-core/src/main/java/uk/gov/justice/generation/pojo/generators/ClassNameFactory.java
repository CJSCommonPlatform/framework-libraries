package uk.gov.justice.generation.pojo.generators;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.TypeNamePluginProcessor;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Generates the correct {@link TypeName} for the specifed {@link Definition}.
 * Used for generating the correct return types and parameters. Can handle
 * generic types.
 *
 * The behaviour can be modified using {@link uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin}s
 */
public class ClassNameFactory {

    private final TypeNameProvider typeNameProvider;
    private final TypeNamePluginProcessor typeNamePluginProcessor;

    public ClassNameFactory(
            final TypeNameProvider typeNameProvider,
            final TypeNamePluginProcessor typeNamePluginProcessor) {
        this.typeNameProvider = typeNameProvider;
        this.typeNamePluginProcessor = typeNamePluginProcessor;
    }

    /**
     * Generate to correct return type/parameter type for the specified {@link Definition}
     *
     * @param definition The definition for which to generate the correct return type
     * @param pluginContext The {@link PluginContext}
     *                      
     * @return The correct type for returns and parameters
     */
    public TypeName createTypeNameFrom(final Definition definition, final PluginContext pluginContext) {

        final TypeName typeName;
        switch (definition.type()) {
            case ARRAY:
                typeName = typeNameProvider.typeNameForArray(definition, this, pluginContext);
                break;

            case BOOLEAN:
                typeName = typeNameProvider.typeNameForBoolean();
                break;

            case INTEGER:
                typeName = typeNameProvider.typeNameForInteger();
                break;

            case NUMBER:
                typeName = typeNameProvider.typeNameForNumber();
                break;

            case REFERENCE:
                typeName = typeNameProvider.typeNameForReference(definition, this, pluginContext);
                break;

            case STRING:
                typeName = typeNameProvider.typeNameForString();
                break;

            case CLASS:
            case ENUM:
            case COMBINED:
            default:
                typeName = typeNameProvider.typeNameForClass(definition);
        }

        return typeNamePluginProcessor.processTypeNamePlugins(typeName, definition, pluginContext);
    }

    public ClassName createClassNameFrom(final ClassDefinition classDefinition) {
        return typeNameProvider.typeNameForClass(classDefinition);
    }
}
