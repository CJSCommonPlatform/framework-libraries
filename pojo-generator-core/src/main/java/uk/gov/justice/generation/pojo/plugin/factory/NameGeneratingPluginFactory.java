package uk.gov.justice.generation.pojo.plugin.factory;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.PluginProviderException;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.NameGeneratablePlugin;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.RootNameGeneratorPlugin;

import java.util.List;
import java.util.Map;

public class NameGeneratingPluginFactory {

    private static final Class<NameGeneratablePlugin> NAME_GENERATABLE_PLUGIN = NameGeneratablePlugin.class;

    public NameGeneratablePlugin create(final Map<Class<?>, List<Plugin>> pluginTypes) {

        if (pluginTypes.containsKey(NAME_GENERATABLE_PLUGIN)) {
            final List<Plugin> nameGeneratablePlugins = pluginTypes.get(NAME_GENERATABLE_PLUGIN);

            if (nameGeneratablePlugins.size() > 1) {
                final List<String> pluginNames = nameGeneratablePlugins.stream().map(plugin -> plugin.getClass().getSimpleName()).collect(toList());
                throw new PluginProviderException(format("Multiple NameGeneratablePlugin identified, please supply only one. List: %s", pluginNames));
            }

            return (NameGeneratablePlugin) nameGeneratablePlugins.get(0);
        }

        return new RootNameGeneratorPlugin();
    }
}
