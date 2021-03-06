package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.plugin.Plugin;

import java.util.List;

public class AllPluginsInstantiator {

    private final PluginInstantiator pluginInstantiator;

    public AllPluginsInstantiator(final PluginInstantiator pluginInstantiator) {
        this.pluginInstantiator = pluginInstantiator;
    }

    public List<Plugin> instantiate(final List<String> pluginNames) {
        return pluginNames
                .stream()
                .map(pluginInstantiator::instantiate)
                .collect(toList());
    }
}
