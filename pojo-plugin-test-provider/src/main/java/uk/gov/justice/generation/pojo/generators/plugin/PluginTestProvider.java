package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.EventAnnotationPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.FieldAndMethodPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.SerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import java.util.List;

public class PluginTestProvider implements PluginProvider {

    @Override
    public List<ClassGeneratorPlugin> pluginClassGenerators() {
        return asList(
                new EventAnnotationPlugin(),
                new SerializablePlugin(),
                new FieldAndMethodPlugin());
    }

    @Override
    public List<TypeNamePlugin> typeNamePlugins() {
        return emptyList();
    }
}
