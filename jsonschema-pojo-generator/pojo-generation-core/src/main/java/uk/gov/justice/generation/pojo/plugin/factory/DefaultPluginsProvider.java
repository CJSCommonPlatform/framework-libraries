package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin.newAddFieldsAndMethodsToClassPlugin;

import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.BuilderGeneratorFactory;

import java.util.List;

public class DefaultPluginsProvider {

    public List<ClassModifyingPlugin> getDefaultPlugins() {
        return asList(
                newAddFieldsAndMethodsToClassPlugin(),
                new GenerateBuilderForClassPlugin(new BuilderGeneratorFactory())
        );
    }
}
