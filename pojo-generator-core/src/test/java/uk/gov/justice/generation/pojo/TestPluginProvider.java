package uk.gov.justice.generation.pojo;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typemodifying.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typemodifying.TypeModifyingPlugin;

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
