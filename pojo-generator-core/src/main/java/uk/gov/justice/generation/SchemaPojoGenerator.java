package uk.gov.justice.generation;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.PluginProviderFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;

/**
 * Main entry point into the application.
 *
 * Will generate pojos from the specified json schema file.
 *
 * This class is called by maven which will pass in all the maven configuration specified in the
 * maven pom
 */
public class SchemaPojoGenerator implements Generator<File> {

    private final GeneratorContextProvider generatorContextProvider;
    private final ClassNameFactoryProvider classNameFactoryProvider;
    private final JavaGeneratorFactoryProvider javaGeneratorFactoryProvider;
    private final JavaClassFileWriter javaClassFileWriter;
    private final PluginProviderFactory pluginProviderFactory;
    private final DefinitionProvider definitionProvider;

    public SchemaPojoGenerator(final PluginProviderFactory pluginProviderFactory,
                               final DefinitionProvider definitionProvider,
                               final GeneratorContextProvider generatorContextProvider,
                               final ClassNameFactoryProvider classNameFactoryProvider,
                               final JavaGeneratorFactoryProvider javaGeneratorFactoryProvider,
                               final JavaClassFileWriter javaClassFileWriter) {
        this.pluginProviderFactory = pluginProviderFactory;
        this.definitionProvider = definitionProvider;
        this.generatorContextProvider = generatorContextProvider;
        this.classNameFactoryProvider = classNameFactoryProvider;
        this.javaGeneratorFactoryProvider = javaGeneratorFactoryProvider;
        this.javaClassFileWriter = javaClassFileWriter;
    }

    /**
     * Run the pojo generation based on the specified json schema file
     *
     * @param jsonSchemaFile  The json schema file from which to generate pojos
     * @param generatorConfig The configuration specified in the maven pom
     */
    @Override
    public void run(final File jsonSchemaFile, final GeneratorConfig generatorConfig) {

        final GenerationContext generationContext = generatorContextProvider.getGenerationContext(jsonSchemaFile, generatorConfig);
        final PluginProvider pluginProvider = pluginProviderFactory.createFor(generatorConfig);

        final Logger logger = generationContext.getLoggerFor(getClass());
        logger.info("Generating java pojos from schema file '{}'", jsonSchemaFile.getName());

        final ClassNameFactory classNameFactory = classNameFactoryProvider.getClassNameFactory(generationContext, pluginProvider);
        final JavaGeneratorFactory javaGeneratorFactory = javaGeneratorFactoryProvider.getJavaGeneratorFactory(classNameFactory);

        final PluginContext pluginContext = new PluginContext(
                javaGeneratorFactory,
                classNameFactory,
                generationContext.getSourceFilename(),
                pluginProvider.classModifyingPlugins(),
                generatorConfig.getGeneratorProperties());

        final List<Definition> definitions = definitionProvider.createDefinitions(
                jsonSchemaFile,
                pluginProvider,
                pluginContext);

        final List<ClassGeneratable> classGenerators = javaGeneratorFactory.createClassGeneratorsFor(
                definitions,
                pluginProvider,
                pluginContext,
                generationContext);

        javaClassFileWriter.writeJavaClassesToFile(generationContext, classGenerators);
    }
}
