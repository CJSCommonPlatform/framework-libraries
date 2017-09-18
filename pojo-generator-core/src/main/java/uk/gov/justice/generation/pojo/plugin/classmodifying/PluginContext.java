package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import java.util.List;

/**
 * Placeholder for all resources required by any {@link ClassModifyingPlugin} or
 * {@link uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin}
 */
public class PluginContext {
    
    private final JavaGeneratorFactory generatorFactory;
    private final ClassNameFactory classNameFactory;
    private final String sourceFilename;
    private final List<ClassModifyingPlugin> classModifyingPlugins;

    public PluginContext(final JavaGeneratorFactory generatorFactory,
                         final ClassNameFactory classNameFactory,
                         final String sourceFilename,
                         final List<ClassModifyingPlugin> classModifyingPlugins) {
        this.generatorFactory = generatorFactory;
        this.classNameFactory = classNameFactory;
        this.sourceFilename = sourceFilename;
        this.classModifyingPlugins = classModifyingPlugins;
    }

    /**
     * Gets the {@link JavaGeneratorFactory} for the application
     * @return The {@link JavaGeneratorFactory}
     */
    public JavaGeneratorFactory getJavaGeneratorFactory() {
        return generatorFactory;
    }

    /**
     * Gets the {@link JavaGeneratorFactory} for the application
     * @return The {@link JavaGeneratorFactory}
     */
    public ClassNameFactory getClassNameFactory() {
        return classNameFactory;
    }

    /**
     * Gets the name of the json schema file
     * @return The name of the json schema file
     */
    public String getSourceFilename() {
        return sourceFilename;
    }

    /**
     * Will return true if the plugin specified by it's class is configured to
     * be used in the generation of POJOs.
     *
     * The purpose of this is in case one plugin needs to know whether another
     * plugin will also be modifying the class. An example of this is
     * {@link AddToStringMethodToClassPlugin} needs to know if the
     * {@link AddAdditionalPropertiesToClassPlugin} is being used so it can
     * add the 'additionalProperties' Map into the generated toString()
     * method
     *
     * @param pluginClass The class of the Plugin that is in use by the application
     * @return true if the plugin is configured for use
     */
    public boolean isPluginInUse(final Class<? extends ClassModifyingPlugin> pluginClass) {

        final List<Class<?>> pluginClasses = classModifyingPlugins
                .stream()
                .map(ClassModifyingPlugin::getClass)
                .collect(toList());

        return pluginClasses.contains(pluginClass);
    }
}
