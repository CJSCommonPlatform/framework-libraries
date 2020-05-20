package uk.gov.justice.generation.pojo.plugin;

import java.util.List;

public class PluginVerifier {

    public void verifyCompatibility(final List<Plugin> allPlugins, final List<String> pluginNames) {

        allPlugins.forEach(plugin -> plugin.checkCompatibilityWith(pluginNames));
    }
}
