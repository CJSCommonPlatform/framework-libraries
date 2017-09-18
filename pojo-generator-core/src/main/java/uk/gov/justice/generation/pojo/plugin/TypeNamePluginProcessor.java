package uk.gov.justice.generation.pojo.plugin;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import com.squareup.javapoet.TypeName;

public class TypeNamePluginProcessor {

    private final PluginProvider pluginProvider;

    public TypeNamePluginProcessor(final PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    /**
     * Runs the list of {@link TypeModifyingPlugin}s against the specified {@link Definition}
     *
     * @param originalTypeName The {@link TypeName} to be modified
     * @param definition The definition who's {@link TypeName}s are to be modified
     * @return The modified {@link TypeName}
     */
    public TypeName processTypeNamePlugins(final TypeName originalTypeName, final Definition definition) {

        TypeName decoratedTypeName = originalTypeName;
        for (final TypeModifyingPlugin typeModifyingPlugin : pluginProvider.typeModifyingPlugins()) {
            decoratedTypeName = typeModifyingPlugin.modifyTypeName(decoratedTypeName, definition);
        }

        return decoratedTypeName;
    }
}
