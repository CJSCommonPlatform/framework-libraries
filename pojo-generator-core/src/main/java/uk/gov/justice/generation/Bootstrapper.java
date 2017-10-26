package uk.gov.justice.generation;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.PluginProviderFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.provider.GeneratorContextProvider;
import uk.gov.justice.generation.provider.PluginContextProvider;
import uk.gov.justice.generation.provider.PojoGeneratorFactoriesProvider;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.List;

import org.everit.json.schema.Schema;

public class Bootstrapper {

    private final PluginContextProvider pluginContextProvider;
    private final PojoGeneratorFactoriesProvider pojoGeneratorFactoriesProvider;
    private final PluginProviderFactory pluginProviderFactory;
    private final GeneratorContextProvider generatorContextProvider;

    public Bootstrapper(
            final PluginContextProvider pluginContextProvider,
            final PojoGeneratorFactoriesProvider pojoGeneratorFactoriesProvider,
            final PluginProviderFactory pluginProviderFactory,
            final GeneratorContextProvider generatorContextProvider) {
        this.pluginContextProvider = pluginContextProvider;
        this.pojoGeneratorFactoriesProvider = pojoGeneratorFactoriesProvider;
        this.pluginProviderFactory = pluginProviderFactory;
        this.generatorContextProvider = generatorContextProvider;
    }

    public PluginContext getPluginContext(
            final PojoGeneratorProperties generatorProperties,
            final GenerationContext generationContext,
            final PluginProvider pluginProvider,
            final ClassNameFactory classNameFactory,
            final JavaGeneratorFactory javaGeneratorFactory) {

        final String sourceFilename = generationContext.getSourceFilename();
        final List<ClassModifyingPlugin> classModifyingPlugins = pluginProvider.classModifyingPlugins();

        return pluginContextProvider.create(
                javaGeneratorFactory,
                classNameFactory,
                sourceFilename,
                classModifyingPlugins,
                generatorProperties
        );
    }

    public JavaGeneratorFactory getJavaGeneratorFactory(final ClassNameFactory classNameFactory) {
        return pojoGeneratorFactoriesProvider.createJavaGeneratorFactory(classNameFactory);
    }

    public ClassNameFactory getClassNameFactory(final GenerationContext generationContext, final PluginProvider pluginProvider) {
        return pojoGeneratorFactoriesProvider.createClassNameFactory(generationContext, pluginProvider);
    }

    public PluginProvider getPluginProvider(final PojoGeneratorProperties generatorProperties) {
        return pluginProviderFactory.createFor(generatorProperties);
    }

    public GenerationContext getGenerationContext(final GeneratorConfig generatorConfig, final Schema schema, final String schemaFileName) {
        return generatorContextProvider.create(schemaFileName, schema, generatorConfig);
    }
}
