package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.write.JavaClassFileWriter;
import uk.gov.justice.generation.provider.DefinitionsFactory;
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
 * This class is called by maven which will pass in all the maven configuration specified in the
 * maven pom
 */
public class SchemaPojoGenerator implements Generator<File> {

    public static final String ROOT_FIELD_NAME = "ROOT";
    private final JavaClassFileWriter javaClassFileWriter;
    private final DefinitionsFactory definitionsFactory;
    private final SchemaLoader schemaLoader;
    private final VisitableFactory visitableFactory;
    private final AcceptorService acceptorService;
    private final Bootstrapper bootstrapper;

    public SchemaPojoGenerator(final DefinitionsFactory definitionsFactory,
                               final JavaClassFileWriter javaClassFileWriter,
                               final SchemaLoader schemaLoader,
                               final VisitableFactory visitableFactory,
                               final AcceptorService acceptorService,
                               final Bootstrapper bootstrapper) {
        this.definitionsFactory = definitionsFactory;
        this.javaClassFileWriter = javaClassFileWriter;
        this.schemaLoader = schemaLoader;
        this.visitableFactory = visitableFactory;
        this.acceptorService = acceptorService;
        this.bootstrapper = bootstrapper;
    }

    /**
     * Run the pojo generation based on the specified json schema file
     *
     * @param jsonSchemaFile  The json schema file from which to generate pojos
     * @param generatorConfig The configuration specified in the maven pom
     */
    @Override
    public void run(final File jsonSchemaFile, final GeneratorConfig generatorConfig) {

        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final PojoGeneratorProperties generatorProperties = (PojoGeneratorProperties) generatorConfig.getGeneratorProperties();
        final String schemaFileName = jsonSchemaFile.getName();

        final GenerationContext generationContext = bootstrapper.getGenerationContext(generatorConfig, schema, schemaFileName);
        final PluginProvider pluginProvider = bootstrapper.getPluginProvider(generatorProperties);
        final ClassNameFactory classNameFactory = bootstrapper.getClassNameFactory(generationContext, pluginProvider);
        final JavaGeneratorFactory javaGeneratorFactory = bootstrapper.getJavaGeneratorFactory(classNameFactory);
        final PluginContext pluginContext = bootstrapper.getPluginContext(
                generatorProperties,
                generationContext,
                pluginProvider,
                classNameFactory,
                javaGeneratorFactory);

        final Logger logger = generationContext.getLoggerFor(getClass());
        logger.info("Generating java pojos from schema file '{}'", schemaFileName);

        final Visitable visitable = visitableFactory.createWith(ROOT_FIELD_NAME, schema, acceptorService);
        final List<Definition> definitions = definitionsFactory.createDefinitions(visitable);
        final List<ClassGeneratable> classGenerators = javaGeneratorFactory.createClassGeneratorsFor(
                definitions,
                pluginProvider,
                pluginContext,
                generationContext);

        javaClassFileWriter.writeJavaClassesToFile(generationContext, classGenerators);
    }
}
