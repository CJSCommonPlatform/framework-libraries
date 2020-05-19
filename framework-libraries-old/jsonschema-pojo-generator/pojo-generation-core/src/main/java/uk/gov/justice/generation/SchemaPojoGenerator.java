package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.parser.SchemaDefinition;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.classmodifying.hashcode.SerialVersionUIDGenerator;
import uk.gov.justice.generation.pojo.validation.SchemaValidatorVisitor;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.write.JavaClassFileWriter;
import uk.gov.justice.generation.provider.DefinitionsFactory;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

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
public class SchemaPojoGenerator implements Generator<SchemaDefinition> {

    private static final String ROOT_FIELD_NAME = "ROOT";

    private final JavaClassFileWriter javaClassFileWriter;
    private final DefinitionsFactory definitionsFactory;
    private final VisitableFactory visitableFactory;
    private final AcceptorService acceptorService;
    private final Bootstrapper bootstrapper;
    private final SchemaValidatorVisitor schemaValidatorVisitor;
    private final SerialVersionUIDGenerator serialVersionUIDGenerator;

    public SchemaPojoGenerator(final DefinitionsFactory definitionsFactory,
                               final JavaClassFileWriter javaClassFileWriter,
                               final VisitableFactory visitableFactory,
                               final AcceptorService acceptorService,
                               final Bootstrapper bootstrapper,
                               final SchemaValidatorVisitor schemaValidatorVisitor,
                               final SerialVersionUIDGenerator serialVersionUIDGenerator) {
        this.definitionsFactory = definitionsFactory;
        this.javaClassFileWriter = javaClassFileWriter;
        this.visitableFactory = visitableFactory;
        this.acceptorService = acceptorService;
        this.bootstrapper = bootstrapper;
        this.schemaValidatorVisitor = schemaValidatorVisitor;
        this.serialVersionUIDGenerator = serialVersionUIDGenerator;
    }

    /**
     * Run the pojo generation based on the specified json schema file
     *
     * @param schemaDefinition The container with a schema from which to generate pojos
     * @param generatorConfig  The configuration specified in the maven pom
     */
    @Override
    public void run(final SchemaDefinition schemaDefinition, final GeneratorConfig generatorConfig) {

        final Schema schema = schemaDefinition.getSchema();

        final PojoGeneratorProperties generatorProperties = (PojoGeneratorProperties) generatorConfig.getGeneratorProperties();
        final String schemaFileName = schemaDefinition.getFilename();

        final GenerationContext generationContext = bootstrapper.getGenerationContext(generatorConfig, schema, schemaFileName);
        final PluginProvider pluginProvider = bootstrapper.getPluginProvider(generatorProperties);
        final ClassNameFactory classNameFactory = bootstrapper.getClassNameFactory(generationContext, pluginProvider);
        final JavaGeneratorFactory javaGeneratorFactory = bootstrapper.getJavaGeneratorFactory(classNameFactory);
        final long serialVersionUID = serialVersionUIDGenerator.generateHashCode(schema);
        final PluginContext pluginContext = bootstrapper.getPluginContext(
                generatorProperties,
                generationContext,
                pluginProvider,
                classNameFactory,
                javaGeneratorFactory,
                serialVersionUID);

        final Logger logger = generationContext.getLoggerFor(getClass());
        logger.info("Generating java pojos from schema file '{}'", schemaFileName);

        final Visitable visitable = visitableFactory.createWith(ROOT_FIELD_NAME, schema, acceptorService);

        visitable.accept(schemaValidatorVisitor);

        final List<Definition> definitions = definitionsFactory.createDefinitions(visitable);
        final List<ClassGeneratable> classGenerators = javaGeneratorFactory.createClassGeneratorsFor(
                definitions,
                pluginProvider,
                pluginContext,
                generationContext);

        javaClassFileWriter.writeJavaClassesToFile(generationContext, classGenerators);
    }
}
