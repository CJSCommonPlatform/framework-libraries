package uk.gov.justice.generation.pojo.integration.utils;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.TypeNameProvider;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.TypeNamePluginProcessor;

public class GeneratorFactoryBuilder {

    private GenerationContext generationContext;
    private PluginProvider pluginProvider;

    public GeneratorFactoryBuilder withGenerationContext(final GenerationContext generationContext) {
        this.generationContext = generationContext;
        return this;
    }

    public GeneratorFactoryBuilder withPluginProvider(final PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
        return this;
    }

    public JavaGeneratorFactory build() {

        if (generationContext == null) {
            throw new RuntimeException("Please set the GenerationContext before calling build()");
        }

        if (pluginProvider == null) {
            throw new RuntimeException("Please set the PluginProvider before calling build()");
        }


        final TypeNameProvider typeNameProvider = new TypeNameProvider(generationContext);
        final TypeNamePluginProcessor typeNamePluginProcessor = new TypeNamePluginProcessor(pluginProvider);
        final ClassNameFactory classNameFactory = new ClassNameFactory(typeNameProvider, typeNamePluginProcessor);

        return new JavaGeneratorFactory(classNameFactory);
    }
}
