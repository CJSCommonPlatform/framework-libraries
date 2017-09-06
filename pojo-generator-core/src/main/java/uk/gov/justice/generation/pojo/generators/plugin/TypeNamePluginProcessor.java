package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import com.squareup.javapoet.TypeName;

public class TypeNamePluginProcessor {

    private final PluginProvider pluginProvider;

    public TypeNamePluginProcessor(final PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    public TypeName processTypeNamePlugins(final TypeName originalTypeName, final Definition definition) {

        TypeName decoratedTypeName = originalTypeName;
        for (final TypeNamePlugin typeNamePlugin : pluginProvider.typeNamePlugins()) {
            decoratedTypeName = typeNamePlugin.modifyTypeName(decoratedTypeName, definition);
        }

        return decoratedTypeName;
    }
}
