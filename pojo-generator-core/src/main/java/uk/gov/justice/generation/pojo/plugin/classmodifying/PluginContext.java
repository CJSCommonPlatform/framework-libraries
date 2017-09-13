package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import java.util.List;

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

    public JavaGeneratorFactory getJavaGeneratorFactory() {
        return generatorFactory;
    }

    public ClassNameFactory getClassNameFactory() {
        return classNameFactory;
    }

    public String getSourceFilename() {
        return sourceFilename;
    }

    public boolean isPluginInUse(final Class<? extends ClassModifyingPlugin> pluginClass) {

        final List<Class<?>> pluginClasses = classModifyingPlugins
                .stream()
                .map(ClassModifyingPlugin::getClass)
                .collect(toList());

        return pluginClasses.contains(pluginClass);
    }
}
