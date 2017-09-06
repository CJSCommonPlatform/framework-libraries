package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

public class PluginTestProvider implements PluginProvider {

    @Override
    public List<PluginClassGeneratable> pluginClassGenerators() {
        return asList(
                new EventAnnotationGenerator(),
                new SerializableGenerator(),
                new FieldAndMethodGenerator());
    }

    @Override
    public List<TypeNamePlugin> typeNamePlugins() {
        return emptyList();
    }
}
