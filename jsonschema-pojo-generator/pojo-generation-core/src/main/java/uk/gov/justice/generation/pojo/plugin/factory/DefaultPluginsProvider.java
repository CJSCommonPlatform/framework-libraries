package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin.newAddFieldsAndMethodsToClassPlugin;

import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.BuilderGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.GetterStatementGenerator;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.OptionalTypeNameUtil;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.WithMethodGenerator;

import java.util.List;

public class DefaultPluginsProvider {

    public List<ClassModifyingPlugin> getDefaultPlugins() {
        final OptionalTypeNameUtil optionalTypeNameUtil = new OptionalTypeNameUtil();
        final GetterStatementGenerator getterStatementGenerator = new GetterStatementGenerator(optionalTypeNameUtil);
        return asList(
                newAddFieldsAndMethodsToClassPlugin(),
                new GenerateBuilderForClassPlugin(
                        new BuilderGeneratorFactory(),
                        new WithMethodGenerator(optionalTypeNameUtil, getterStatementGenerator))
        );
    }
}
