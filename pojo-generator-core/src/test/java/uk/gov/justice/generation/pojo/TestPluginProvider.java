package uk.gov.justice.generation.pojo;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import uk.gov.justice.generation.pojo.generators.plugin.FieldAndMethodGenerator;
import uk.gov.justice.generation.pojo.generators.plugin.OptionalTypeNamePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.PluginClassGeneratable;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.SerializableGenerator;
import uk.gov.justice.generation.pojo.generators.plugin.TypeNamePlugin;

import java.util.List;

@SuppressWarnings("unused")
public class TestPluginProvider implements PluginProvider {

    @Override
    public List<PluginClassGeneratable> pluginClassGenerators() {
        return asList(
                new SerializableGenerator(),
                new FieldAndMethodGenerator());
    }

    @Override
    public List<TypeNamePlugin> typeNamePlugins() {
        return singletonList(new OptionalTypeNamePlugin());
    }
}
