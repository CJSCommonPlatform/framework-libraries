package uk.gov.justice.generation.pojo.integration.utils;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.OptionalTypeNameUtil;

public class GeneratorFactoryBuilder {

    private GenerationContext generationContext;
    private PluginProvider pluginProvider;
    private ClassNameFactory classNameFactory;
    private OptionalTypeNameUtil optionalTypeNameUtil;

    public GeneratorFactoryBuilder withGenerationContext(final GenerationContext generationContext) {
        this.generationContext = generationContext;
        return this;
    }

    public GeneratorFactoryBuilder withPluginProvider(final PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
        return this;
    }

    public GeneratorFactoryBuilder withClassNameFactory(final ClassNameFactory classNameFactory) {
        this.classNameFactory = classNameFactory;
        return this;
    }

    public GeneratorFactoryBuilder withOptionalTypeNameUtil(final OptionalTypeNameUtil optionalTypeNameUtil) {
        this.optionalTypeNameUtil = optionalTypeNameUtil;
        return this;
    }

    public JavaGeneratorFactory build() {

        if (generationContext == null) {
            throw new RuntimeException("Please set the GenerationContext before calling build()");
        }

        if (pluginProvider == null) {
            throw new RuntimeException("Please set the PluginProvider before calling build()");
        }

        if (classNameFactory == null) {
            throw new RuntimeException("Please set the ClassNameFactory before calling build()");
        }

        return new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil);
    }
}
