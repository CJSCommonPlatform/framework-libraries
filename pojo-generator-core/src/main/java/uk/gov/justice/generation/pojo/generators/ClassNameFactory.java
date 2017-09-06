package uk.gov.justice.generation.pojo.generators;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.plugin.TypeNamePluginProcessor;

import com.squareup.javapoet.TypeName;

public class ClassNameFactory {

    private final TypeNameProvider typeNameProvider;
    private final TypeNamePluginProcessor typeNamePluginProcessor;

    public ClassNameFactory(
            final TypeNameProvider typeNameProvider,
            final TypeNamePluginProcessor typeNamePluginProcessor) {
        this.typeNameProvider = typeNameProvider;
        this.typeNamePluginProcessor = typeNamePluginProcessor;
    }

    public TypeName createTypeNameFrom(final Definition definition) {

        final TypeName typeName;
        switch (definition.type()) {
            case ARRAY:
                typeName = typeNameProvider.typeNameForArray(definition, this);
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
                typeName = typeNameProvider.typeNameForReference(definition, this);
                break;

            case STRING:
                typeName = typeNameProvider.typeNameForString();
                break;

            case CLASS:
            case ENUM:
            case COMBINED:
            case ROOT:
            default:
                typeName = typeNameProvider.typeNameForClass(definition);
        }

        return typeNamePluginProcessor.processTypeNamePlugins(typeName, definition);
    }
}
