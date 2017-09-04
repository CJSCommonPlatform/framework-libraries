package uk.gov.justice.generation.pojo;

import static java.util.Arrays.asList;

import uk.gov.justice.generation.pojo.generators.plugin.FieldAndMethodGenerator;
import uk.gov.justice.generation.pojo.generators.plugin.PluginClassGeneratable;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.SerializableGenerator;

import java.util.List;

public class TestPluginProvider implements PluginProvider {

    @Override
    public List<PluginClassGeneratable> pluginClassGenerators() {
        return asList(
                new SerializableGenerator(),
                new FieldAndMethodGenerator());
    }
}
