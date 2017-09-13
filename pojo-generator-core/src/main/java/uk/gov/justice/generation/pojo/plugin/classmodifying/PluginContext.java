package uk.gov.justice.generation.pojo.plugin.classmodifying;

import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

public class PluginContext {
    
    private final JavaGeneratorFactory generatorFactory;
    private final ClassNameFactory classNameFactory;
    private final String sourceFilename;

    public PluginContext(final JavaGeneratorFactory generatorFactory,
                         final ClassNameFactory classNameFactory,
                         final String sourceFilename) {
        this.generatorFactory = generatorFactory;
        this.classNameFactory = classNameFactory;
        this.sourceFilename = sourceFilename;
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
}
