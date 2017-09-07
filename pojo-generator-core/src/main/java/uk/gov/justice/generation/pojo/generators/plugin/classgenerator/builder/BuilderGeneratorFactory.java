package uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;

public class BuilderGeneratorFactory {

    public BuilderGenerator create(final ClassDefinition classDefinition,
                                   final ClassNameFactory classNameFactory,
                                   final GenerationContext generationContext) {

        return new BuilderGenerator(
                classDefinition,
                classNameFactory,
                generationContext,
                new BuilderFieldFactory(),
                new BuilderMethodFactory()
        );
    }
}
