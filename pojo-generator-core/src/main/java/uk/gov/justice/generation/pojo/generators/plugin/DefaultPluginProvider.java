package uk.gov.justice.generation.pojo.generators.plugin;

import static java.util.Arrays.asList;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder.BuilderGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.typename.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.SupportUuidsPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.SupportZonedDateTimePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import java.util.List;

public class DefaultPluginProvider implements PluginProvider {

    @Override
    public List<ClassModifyingPlugin> classModifyingPlugins() {
        return asList(
                new MakeClassSerializablePlugin(),
                new AddFieldsAndMethodsToClassPlugin(),
                new GenerateBuilderForClassPlugin(new BuilderGeneratorFactory()));
    }

    @Override
    public List<TypeModifyingPlugin> typeModifyingPlugins() {
        return asList(
                new SupportJavaOptionalsPlugin(),
                new SupportUuidsPlugin(),
                new SupportZonedDateTimePlugin());
    }


}
