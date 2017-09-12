package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import com.squareup.javapoet.TypeName;

public class TypeNamePluginProcessor {

    private final PluginProvider pluginProvider;

    public TypeNamePluginProcessor(final PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    public TypeName processTypeNamePlugins(final TypeName originalTypeName, final Definition definition) {

        TypeName decoratedTypeName = originalTypeName;
        for (final TypeModifyingPlugin typeModifyingPlugin : pluginProvider.typeModifyingPlugins()) {
            decoratedTypeName = typeModifyingPlugin.modifyTypeName(decoratedTypeName, definition);
        }

        return decoratedTypeName;
    }
}
