package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.EventAnnotationPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.FieldAndMethodPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.SerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder.BuilderGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder.BuilderPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.OptionalTypeNamePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import java.util.List;

public class DefaultPluginProvider implements PluginProvider {

    @Override
    public List<ClassGeneratorPlugin> pluginClassGenerators() {
        return asList(
                new EventAnnotationPlugin(),
                new SerializablePlugin(),
                new FieldAndMethodPlugin(),
                new BuilderPlugin(new BuilderGeneratorFactory()));
    }

    @Override
    public List<TypeNamePlugin> typeNamePlugins() {
        return singletonList(new OptionalTypeNamePlugin());
    }
}
