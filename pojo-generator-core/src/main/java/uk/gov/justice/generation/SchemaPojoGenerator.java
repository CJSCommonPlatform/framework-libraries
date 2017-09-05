package uk.gov.justice.generation;

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
import uk.gov.justice.generation.pojo.validation.FileNameValidator;
import uk.gov.justice.generation.pojo.visitable.VisitableSchemaFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorFactory;
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

public class SchemaPojoGenerator implements Generator<File> {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final NameGenerator nameGenerator = new NameGenerator();
    private final FileNameValidator fileNameValidator = new FileNameValidator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final VisitableSchemaFactory visitableSchemaFactory = new VisitableSchemaFactory();

    @Override
    public void run(final File source, final GeneratorConfig generatorConfig) {
        final String packageName = generatorConfig.getBasePackageName();
        final GenerationContext generationContext = new GenerationContext(generatorConfig.getOutputDirectory(), packageName);
        final Logger logger = generationContext.getLoggerFor(getClass());

        logger.info("Generating java pojos from schema file '{}'", source.getName());

        final List<Definition> definitions = createDefinitions(source);

        final List<ClassGeneratable> classGenerators = getClassGeneratorsFrom(generatorConfig, generationContext, definitions);

        writeJavaClassesToFile(generationContext, classGenerators);
    }

    private List<Definition> createDefinitions(final File source) {
        final DefinitionBuilderVisitor definitionBuilderVisitor = constructDefinitionBuilderVisitor(source);
        final Schema schema = schemaLoader.loadFrom(source);
        final String fieldName = nameGenerator.rootFieldNameFrom(source);
        final DefaultAcceptorFactory jsonSchemaAcceptorFactory = new DefaultAcceptorFactory(visitableSchemaFactory);

        visitableSchemaFactory.createWith(schema, jsonSchemaAcceptorFactory)
                .accept(fieldName, definitionBuilderVisitor);

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

        return javaGeneratorFactory.createClassGeneratorsFor(definitions, pluginProvider);
    }

    private void writeJavaClassesToFile(final GenerationContext generationContext, final List<ClassGeneratable> classGenerators) {
        final NonDuplicatingSourceWriter writer = new NonDuplicatingSourceWriter(new JavaSourceFileProvider(), sourceWriter);
        classGenerators.forEach(classGeneratable -> writer.write(classGeneratable, generationContext));
    }

    private DefinitionBuilderVisitor constructDefinitionBuilderVisitor(final File source) {
        if (fileNameValidator.isEventSchema(source)) {
            return new DefinitionBuilderVisitor(new DefaultDefinitionFactory(nameGenerator.eventNameFrom(source)));
        }

        return new DefinitionBuilderVisitor(new DefaultDefinitionFactory());
    }
}
