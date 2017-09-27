package uk.gov.justice.generation.pojo.integration.utils;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.TypeNameProvider;
import uk.gov.justice.generation.pojo.plugin.ModifyingPluginProvider;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.TypeNamePluginProcessor;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddHashcodeAndEqualsPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddToStringMethodToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.BuilderGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.RootNameGeneratorPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.CustomReturnTypePlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.visitor.DefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.ReferenceValueParser;
import uk.gov.justice.generation.pojo.write.SourceWriter;

import java.io.File;
import java.util.List;

import org.everit.json.schema.Schema;
import org.json.JSONObject;

public class GeneratorUtil {

    private final DefinitionFactory definitionFactory = new DefaultDefinitionFactory(new ReferenceValueParser());
    private final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
    private final VisitableFactory visitableFactory = new VisitableFactory();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final AcceptorService acceptorService = new DefaultAcceptorService(visitableFactory);

    private List<String> ignoredClassNames = emptyList();
    private PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();

    private List<ClassModifyingPlugin> classModifyingPlugins = asList(
            new MakeClassSerializablePlugin(),
            new AddFieldsAndMethodsToClassPlugin(),
            new GenerateBuilderForClassPlugin(new BuilderGeneratorFactory()),
            new AddAdditionalPropertiesToClassPlugin(),
            new AddHashcodeAndEqualsPlugin(new AdditionalPropertiesDeterminer()),
            new AddToStringMethodToClassPlugin(new AdditionalPropertiesDeterminer())
    );
    private List<TypeModifyingPlugin> typeModifyingPlugins = asList(
            new SupportJavaOptionalsPlugin(),
            CustomReturnTypePlugin.create());

    public GeneratorUtil withIgnoredClassNames(final List<String> ignoredClassNames) {
        this.ignoredClassNames = ignoredClassNames;
        return this;
    }

    public GeneratorUtil withClassModifyingPlugins(final List<ClassModifyingPlugin> classModifyingPlugins) {
        this.classModifyingPlugins = classModifyingPlugins;
        return this;
    }

    public GeneratorUtil withTypeModifyingPlugins(final List<TypeModifyingPlugin> typeModifyingPlugins) {
        this.typeModifyingPlugins = typeModifyingPlugins;
        return this;
    }

    public GeneratorUtil withGeneratorProperties(final PojoGeneratorProperties generatorProperties) {
        this.generatorProperties = generatorProperties;
        return this;
    }

    public List<Class<?>> generateAndCompileJavaSource(final File jsonSchemaFile,
                                                       final String packageName,
                                                       final OutputDirectories outputDirectories) {

        final GenerationContext generationContext = new GenerationContext(
                outputDirectories.getSourceOutputDirectory(),
                packageName,
                jsonSchemaFile.getName(),
                ignoredClassNames);

        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final PluginProvider pluginProvider = new ModifyingPluginProvider(
                classModifyingPlugins,
                typeModifyingPlugins,
                new RootNameGeneratorPlugin());

        final TypeNameProvider typeNameProvider = new TypeNameProvider(generationContext);
        final TypeNamePluginProcessor typeNamePluginProcessor = new TypeNamePluginProcessor(pluginProvider);
        final ClassNameFactory classNameFactory = new ClassNameFactory(typeNameProvider, typeNamePluginProcessor);

        final GeneratorFactoryBuilder generatorFactoryBuilder = new GeneratorFactoryBuilder();

        final JavaGeneratorFactory javaGeneratorFactory = generatorFactoryBuilder
                .withGenerationContext(generationContext)
                .withPluginProvider(pluginProvider)
                .withClassNameFactory(classNameFactory)
                .build();

        final PluginContext pluginContext = new PluginContext(
                javaGeneratorFactory,
                classNameFactory,
                generationContext.getSourceFilename(),
                classModifyingPlugins,
                generatorProperties);

        final String fieldName = pluginProvider
                .nameGeneratablePlugin()
                .rootFieldNameFrom(schema, jsonSchemaFile.getName(), pluginContext);

        final Visitable visitableSchema = visitableFactory.createWith(fieldName, schema, acceptorService);

        visitableSchema.accept(definitionBuilderVisitor);

        final SourceWriter sourceWriter = new SourceWriter();
        final ClassCompiler classCompiler = new ClassCompiler();

        return javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor.getDefinitions(), pluginProvider, pluginContext, generationContext)
                .stream()
                .map(classGeneratable -> writeAndCompile(
                        outputDirectories,
                        generationContext,
                        sourceWriter,
                        classCompiler,
                        classGeneratable))
                .collect(toList());
    }

    public void validate(final File schemaFile, final String json) {
        final Schema schema = schemaLoader.loadFrom(schemaFile);
        schema.validate(new JSONObject(json));
    }

    private Class<?> writeAndCompile(final OutputDirectories outputDirectories, final GenerationContext generationContext, final SourceWriter sourceWriter, final ClassCompiler classCompiler, final ClassGeneratable classGeneratable) {
        sourceWriter.write(classGeneratable, generationContext);
        return classCompiler.compile(classGeneratable, generationContext, outputDirectories.getClassesOutputDirectory().toFile());
    }
}
