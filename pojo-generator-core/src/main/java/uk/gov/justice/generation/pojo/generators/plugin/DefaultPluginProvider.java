package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;

import java.util.List;

public class DefaultPluginProvider implements PluginProvider {

    @Override
    public List<PluginClassGeneratable> pluginClassGenerators() {
        return asList(
                new EventAnnotationGenerator(),
                new SerializableGenerator(),
                new FieldAndMethodGenerator());
    }
}
