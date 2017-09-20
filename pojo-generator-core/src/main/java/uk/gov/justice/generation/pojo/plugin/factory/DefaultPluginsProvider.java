package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;

import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.BuilderGeneratorFactory;

import java.util.List;

public class DefaultPluginsProvider {

    public List<ClassModifyingPlugin> getDefaultPlugins() {
        return asList(
                new AddFieldsAndMethodsToClassPlugin(),
                new GenerateBuilderForClassPlugin(new BuilderGeneratorFactory())
        );
    }
}
