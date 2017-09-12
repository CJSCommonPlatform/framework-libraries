package uk.gov.justice.generation.pojo.generators.plugin.providers;

import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import java.util.List;

public class TestPrivateConstructorPlugProvider implements PluginProvider {

    private TestPrivateConstructorPlugProvider() {
    }

    @Override
    public List<ClassModifyingPlugin> classModifyingPlugins() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeModifyingPlugin> typeModifyingPlugins() {
        throw new UnsupportedOperationException();
    }
}
