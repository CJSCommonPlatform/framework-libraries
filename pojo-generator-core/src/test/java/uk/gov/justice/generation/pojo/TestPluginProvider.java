package uk.gov.justice.generation.pojo;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.FieldAndMethodPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.SerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.OptionalTypeNamePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import java.util.List;

@SuppressWarnings("unused")
public class TestPluginProvider implements PluginProvider {

    @Override
    public List<ClassGeneratorPlugin> pluginClassGenerators() {
        return asList(
                new SerializablePlugin(),
                new FieldAndMethodPlugin());
    }

    @Override
    public List<TypeNamePlugin> typeNamePlugins() {
        return singletonList(new OptionalTypeNamePlugin());
    }
}
