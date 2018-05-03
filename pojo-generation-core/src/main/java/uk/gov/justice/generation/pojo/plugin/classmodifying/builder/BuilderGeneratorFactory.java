package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

/**
 * Factory for creating a {@link BuilderGenerator}
 */
public class BuilderGeneratorFactory {

    /**
     * Creates a {@link BuilderGenerator}
     *
     * @param classDefinition The {@link ClassDefinition} of the outer POJO which will contain
     *                        the Builder as a static inner class
     * @param classNameFactory A factory for creating the field and method class names
     *
     * @return a newly constructed {@link BuilderGenerator}
     */
    public BuilderGenerator create(final ClassDefinition classDefinition,
                                   final ClassNameFactory classNameFactory,
                                   final PluginContext pluginContext) {

        return new BuilderGenerator(
                classDefinition,
                classNameFactory,
                new BuilderFieldFactory(),
                new BuilderMethodFactory(),
                new AdditionalPropertiesDeterminer(),
                pluginContext
        );
    }
}
