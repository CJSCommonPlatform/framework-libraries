package uk.gov.justice.generation.pojo.generators.plugin.providers;

import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import java.util.List;

public class TestPrivateConstructorPlugProvider implements PluginProvider {

    private TestPrivateConstructorPlugProvider() {
    }

    @Override
    public List<ClassGeneratorPlugin> pluginClassGenerators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeNamePlugin> typeNamePlugins() {
        throw new UnsupportedOperationException();
    }
}
