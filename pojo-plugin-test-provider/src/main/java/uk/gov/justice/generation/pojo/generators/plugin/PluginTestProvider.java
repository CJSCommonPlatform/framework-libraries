package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.AddEventAnnotationToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import java.util.List;

public class PluginTestProvider implements PluginProvider {

    @Override
    public List<ClassModifyingPlugin> classModifyingPlugins() {
        return asList(
                new AddEventAnnotationToClassPlugin(),
                new MakeClassSerializablePlugin(),
                new AddFieldsAndMethodsToClassPlugin());
    }

    @Override
    public List<TypeModifyingPlugin> typeModifyingPlugins() {
        return emptyList();
    }
}
