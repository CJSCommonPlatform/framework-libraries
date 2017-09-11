package uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class BuilderPlugin implements ClassGeneratorPlugin {

    private final BuilderGeneratorFactory builderGeneratorFactory;

    public BuilderPlugin(final BuilderGeneratorFactory builderGeneratorFactory) {
        this.builderGeneratorFactory = builderGeneratorFactory;
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder outerClassBuilder,
            final ClassDefinition classDefinition,
            final JavaGeneratorFactory javaGeneratorFactory,
            final ClassNameFactory classNameFactory,
            final GenerationContext generationContext) {

        final BuilderGenerator builderGenerator = builderGeneratorFactory.create(
                classDefinition,
                classNameFactory);

        final TypeSpec innerClassBuilder = builderGenerator.generate();
        final MethodSpec staticGetBuilderMethod = builderGenerator.generateStaticGetBuilderMethod();

        outerClassBuilder.addType(innerClassBuilder);
        outerClassBuilder.addMethod(staticGetBuilderMethod);

        return outerClassBuilder;
    }
}
