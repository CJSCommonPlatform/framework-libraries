package uk.gov.justice.generation;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;

import java.util.List;

public class PluginContextProvider {

    public PluginContext create(final JavaGeneratorFactory javaGeneratorFactory,
                                final ClassNameFactory classNameFactory,
                                final String sourceFilename,
                                final List<ClassModifyingPlugin> classModifyingPlugins,
                                final PojoGeneratorProperties generatorProperties) {
        return new PluginContext(
                javaGeneratorFactory,
                classNameFactory,
                sourceFilename,
                classModifyingPlugins,
                generatorProperties);
    }
}
