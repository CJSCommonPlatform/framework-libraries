package uk.gov.justice.generation.pojo.generators.plugin.providers;

import uk.gov.justice.generation.pojo.generators.plugin.PluginClassGeneratable;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;

import java.util.List;

public class TestPluginProvider implements PluginProvider {

    @Override
    public List<PluginClassGeneratable> pluginClassGenerators() {
        throw new UnsupportedOperationException();
    }
}
