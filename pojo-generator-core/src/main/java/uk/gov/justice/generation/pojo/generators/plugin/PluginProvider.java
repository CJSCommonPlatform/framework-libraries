package uk.gov.justice.generation.pojo.generators.plugin;

import java.util.List;

public interface PluginProvider {

    List<PluginClassGeneratable> pluginClassGenerators();
}
