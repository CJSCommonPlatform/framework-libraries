package uk.gov.justice.generation.pojo;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import java.util.List;

@SuppressWarnings("unused")
public class TestPluginProvider implements PluginProvider {

    @Override
    public List<ClassModifyingPlugin> classModifyingPlugins() {
        return asList(
                new MakeClassSerializablePlugin(),
                new AddFieldsAndMethodsToClassPlugin());
    }

    @Override
    public List<TypeModifyingPlugin> typeModifyingPlugins() {
        return singletonList(new SupportJavaOptionalsPlugin());
    }
}
