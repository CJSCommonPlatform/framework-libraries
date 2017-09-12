package uk.gov.justice.generation.pojo.generators.plugin.classmodifying.builder;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;

public class BuilderGeneratorFactory {

    public BuilderGenerator create(final ClassDefinition classDefinition,
                                   final ClassNameFactory classNameFactory) {

        return new BuilderGenerator(
                classDefinition,
                classNameFactory,
                new BuilderFieldFactory(),
                new BuilderMethodFactory()
        );
    }
}
