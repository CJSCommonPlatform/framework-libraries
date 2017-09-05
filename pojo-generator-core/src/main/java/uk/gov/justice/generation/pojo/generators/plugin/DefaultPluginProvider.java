package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.List;

public class DefaultPluginProvider implements PluginProvider {

    @Override
    public List<PluginClassGeneratable> pluginClassGenerators() {
        return asList(
                new EventAnnotationGenerator(),
                new SerializableGenerator(),
                new FieldAndMethodGenerator());
    }

    @Override
    public List<TypeNamePlugin> typeNamePlugins() {
        return singletonList(new OptionalTypeNamePlugin());
    }
}
