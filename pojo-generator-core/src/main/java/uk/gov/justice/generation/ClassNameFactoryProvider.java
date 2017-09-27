package uk.gov.justice.generation;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.TypeNameProvider;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.TypeNamePluginProcessor;

public class ClassNameFactoryProvider {

    public ClassNameFactory create(final GenerationContext generationContext, final PluginProvider pluginProvider) {
        final TypeNameProvider typeNameProvider = new TypeNameProvider(generationContext);
        final TypeNamePluginProcessor typeNamePluginProcessor = new TypeNamePluginProcessor(pluginProvider);

        return new ClassNameFactory(typeNameProvider, typeNamePluginProcessor);
    }
}
