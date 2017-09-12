package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.TypeNameProvider;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProviderFactory;
import uk.gov.justice.generation.pojo.generators.plugin.TypeNamePluginProcessor;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.PluginContext;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.write.JavaSourceFileProvider;
import uk.gov.justice.generation.pojo.write.NonDuplicatingSourceWriter;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.util.List;

import org.everit.json.schema.Schema;
import org.slf4j.Logger;

/**
 * Main entry point into the application.
 *
 * Will generate pojos from the specified json schema file.
 *
 * This class is called by maven which will pass in all the maven
 * configuration specified in the maven pom
 */
public class SchemaPojoGenerator implements Generator<File> {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final NameGenerator nameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final VisitableFactory visitableFactory = new VisitableFactory();
    private final JavaFileSimpleNameLister javaFileSimpleNameLister = new JavaFileSimpleNameLister();

    /**
     * Run the pojo generation based on the specified json schema file
     *
     * @param jsonSchemaFile The json schema file from which to generate pojos
     * @param generatorConfig The configuration specified in the maven pom
     */
    @Override
    public void run(final File jsonSchemaFile, final GeneratorConfig generatorConfig) {
        final String packageName = generatorConfig.getBasePackageName();

        final List<String> hardCodedClassNames = javaFileSimpleNameLister.findSimpleNames(
                generatorConfig.getSourcePaths(),
                generatorConfig.getOutputDirectory(),
                generatorConfig.getBasePackageName());

        final GenerationContext generationContext = new GenerationContext(
                generatorConfig.getOutputDirectory(),
                packageName,
                jsonSchemaFile.getName(),
                hardCodedClassNames);

        final Logger logger = generationContext.getLoggerFor(getClass());

        logger.info("Generating java pojos from schema file '{}'", jsonSchemaFile.getName());

        final List<Definition> definitions = createDefinitions(jsonSchemaFile);

        final List<ClassGeneratable> classGenerators = getClassGeneratorsFrom(generatorConfig, generationContext, definitions);

        writeJavaClassesToFile(generationContext, classGenerators);
    }

    private List<Definition> createDefinitions(final File source) {
        final DefinitionBuilderVisitor definitionBuilderVisitor = constructDefinitionBuilderVisitor();
        final Schema schema = schemaLoader.loadFrom(source);
        final String fieldName = nameGenerator.rootFieldNameFrom(source);
        final DefaultAcceptorService jsonSchemaAcceptorFactory = new DefaultAcceptorService(visitableFactory);

        visitableFactory.createWith(fieldName, schema, jsonSchemaAcceptorFactory)
                .accept(definitionBuilderVisitor);

        return definitionBuilderVisitor.getDefinitions();
    }

    private List<ClassGeneratable> getClassGeneratorsFrom(
            final GeneratorConfig generatorConfig,
            final GenerationContext generationContext,
            final List<Definition> definitions) {

        final PluginProviderFactory pluginProviderFactory = new PluginProviderFactory();
        final PluginProvider pluginProvider = pluginProviderFactory.createFor(generatorConfig);

        final TypeNameProvider typeNameProvider = new TypeNameProvider(generationContext);
        final TypeNamePluginProcessor typeNamePluginProcessor = new TypeNamePluginProcessor(pluginProvider);

        final ClassNameFactory classNameFactory = new ClassNameFactory(typeNameProvider, typeNamePluginProcessor);
        final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory(classNameFactory);
        final PluginContext pluginContext = new PluginContext(javaGeneratorFactory, classNameFactory, generationContext.getSourceFilename());

        return javaGeneratorFactory.createClassGeneratorsFor(definitions, pluginProvider, pluginContext, generationContext);
    }

    private void writeJavaClassesToFile(final GenerationContext generationContext, final List<ClassGeneratable> classGenerators) {
        final NonDuplicatingSourceWriter writer = new NonDuplicatingSourceWriter(new JavaSourceFileProvider(), sourceWriter);
        classGenerators.forEach(classGeneratable -> writer.write(classGeneratable, generationContext));
    }

    private DefinitionBuilderVisitor constructDefinitionBuilderVisitor() {
        return new DefinitionBuilderVisitor(new DefaultDefinitionFactory());
    }
}
