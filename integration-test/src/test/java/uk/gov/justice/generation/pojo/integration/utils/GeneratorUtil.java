package uk.gov.justice.generation.pojo.integration.utils;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.visitor.DefinitionFactory;
import uk.gov.justice.generation.pojo.write.SourceWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.everit.json.schema.Schema;

public class GeneratorUtil {

    private final NameGenerator nameGenerator = new NameGenerator();
    private final DefinitionFactory definitionFactory = new DefaultDefinitionFactory();
    private final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
    private final VisitableFactory visitableFactory = new VisitableFactory();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final AcceptorService acceptorService = new DefaultAcceptorService(visitableFactory);

    public List<Class<?>> generateAndCompileJavaSource(final File jsonSchemaFile,
                                                       final String packageName,
                                                       final Path sourceOutputPath,
                                                       final Path classesOutputPath) {

        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        return generateAndCompileJavaSource(jsonSchemaFile, packageName, schema, sourceOutputPath, classesOutputPath);
    }

    public List<Class<?>> generateAndCompileJavaSource(final File jsonSchemaFile,
                                                       final String packageName,
                                                       final Schema schema,
                                                       final Path sourceOutputPath,
                                                       final Path classesOutputPath) {

        final String fieldName = nameGenerator.rootFieldNameFrom(jsonSchemaFile);

        final Visitable visitableSchema = visitableFactory.createWith(fieldName, schema, acceptorService);

        visitableSchema.accept(definitionBuilderVisitor);

        return generateAndCompileFromDefinitions(jsonSchemaFile.getName(), packageName, sourceOutputPath, classesOutputPath, definitionBuilderVisitor.getDefinitions());
    }

    public List<Class<?>> generateAndCompileJavaSource(final GenerationContext generationContext,
                                                       final File jsonSchemaFile,
                                                       final Schema schema,
                                                       final Path classesOutputPath) {


        final String fieldName = nameGenerator.rootFieldNameFrom(jsonSchemaFile);

        final Visitable visitableSchema = visitableFactory.createWith(fieldName, schema, acceptorService);

        visitableSchema.accept(definitionBuilderVisitor);

        return generateAndCompileFromDefinitions(generationContext, classesOutputPath, definitionBuilderVisitor.getDefinitions());
    }

    public List<Class<?>> generateAndCompileFromDefinitions(final String jsonSchemaFilename,
                                                            final String packageName,
                                                            final Path sourceOutputPath,
                                                            final Path classesOutputPath,
                                                            final List<Definition> definitionBuilderVisitor) {
        final GenerationContext generationContext = new GenerationContext(
                sourceOutputPath,
                packageName,
                jsonSchemaFilename,
                emptyList());

        return generateAndCompileFromDefinitions(generationContext, classesOutputPath, definitionBuilderVisitor);
    }

    public List<Class<?>> generateAndCompileFromDefinitions(final GenerationContext generationContext,
                                                            final Path classesOutputPath,
                                                            final List<Definition> definitionBuilderVisitor) {
        final SourceWriter sourceWriter = new SourceWriter();
        final ClassCompiler classCompiler = new ClassCompiler();
        final GeneratorFactoryBuilder generatorFactoryBuilder = new GeneratorFactoryBuilder();
        final PluginProvider pluginProvider = new DefaultPluginProvider();

        final JavaGeneratorFactory javaGeneratorFactory = generatorFactoryBuilder
                .withGenerationContext(generationContext)
                .withPluginProvider(pluginProvider)
                .build();

        return javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor, pluginProvider, generationContext)
                .stream()
                .map(classGeneratable -> {
                    sourceWriter.write(classGeneratable, generationContext);
                    return classCompiler.compile(classGeneratable, generationContext, classesOutputPath.toFile());
                })
                .collect(toList());
    }

}
