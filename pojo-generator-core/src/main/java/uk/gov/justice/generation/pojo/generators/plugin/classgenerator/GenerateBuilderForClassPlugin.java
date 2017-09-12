package uk.gov.justice.generation.pojo.generators.plugin.classgenerator;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.PluginContext;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder.BuilderGenerator;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder.BuilderGeneratorFactory;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class GenerateBuilderForClassPlugin implements ClassModifyingPlugin {

    private final BuilderGeneratorFactory builderGeneratorFactory;

    public GenerateBuilderForClassPlugin(final BuilderGeneratorFactory builderGeneratorFactory) {
        this.builderGeneratorFactory = builderGeneratorFactory;
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder outerClassBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {

        final BuilderGenerator builderGenerator = builderGeneratorFactory.create(
                classDefinition,
                pluginContext.getClassNameFactory());

        final TypeSpec innerClassBuilder = builderGenerator.generate();
        final MethodSpec staticGetBuilderMethod = builderGenerator.generateStaticGetBuilderMethod();

        outerClassBuilder.addType(innerClassBuilder);
        outerClassBuilder.addMethod(staticGetBuilderMethod);

        return outerClassBuilder;
    }
}
