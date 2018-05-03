package uk.gov.justice.generation.pojo.integration.utils;

import static java.nio.file.Paths.get;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin.newAddFieldsAndMethodsToClassPlugin;

import uk.gov.justice.generation.io.files.loader.FileResourceLoader;
import uk.gov.justice.generation.io.files.loader.InputStreamToJsonObjectConverter;
import uk.gov.justice.generation.io.files.loader.Resource;
import uk.gov.justice.generation.io.files.resolver.SchemaCatalogResolver;
import uk.gov.justice.generation.io.files.resolver.SchemaResolver;
import uk.gov.justice.generation.pojo.core.ClassNameParser;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PackageNameParser;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.TypeNameProvider;
import uk.gov.justice.generation.pojo.plugin.ModifyingPluginProvider;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.TypeNamePluginProcessor;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.hashcode.SerialVersionUIDGenerator;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.visitor.DefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.ReferenceValueParser;
import uk.gov.justice.generation.pojo.visitor.StringFormatValueParser;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.schema.catalog.CatalogObjectFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.Schema;
import org.json.JSONObject;

public class GeneratorUtil {

    private static final String ROOT_FIELD_NAME = "ROOT";
    private final DefinitionFactory definitionFactory = new DefaultDefinitionFactory(new ReferenceValueParser(), new StringFormatValueParser());
    private final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
    private final VisitableFactory visitableFactory = new VisitableFactory();

    private final CatalogObjectFactory catalogObjectFactory = new CatalogObjectFactory();

    private final SchemaCatalogResolver schemaCatalogResolver = new SchemaCatalogResolver(
            catalogObjectFactory.rawCatalog(),
            catalogObjectFactory.schemaClientFactory(),
            catalogObjectFactory.jsonToSchemaConverter());

    private final InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter = new InputStreamToJsonObjectConverter();
    private final SchemaResolver schemaResolver = new SchemaResolver(schemaCatalogResolver);
    private final AcceptorService acceptorService = new DefaultAcceptorService(visitableFactory);

    private List<String> ignoredClassNames = emptyList();
    private PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();
    private SerialVersionUIDGenerator serialVersionUIDGenerator = new SerialVersionUIDGenerator();

    private List<ClassModifyingPlugin> classModifyingPlugins = new ArrayList<>();
    private List<TypeModifyingPlugin> typeModifyingPlugins = new ArrayList<>();

    public GeneratorUtil withIgnoredClassNames(final List<String> ignoredClassNames) {
        this.ignoredClassNames = ignoredClassNames;
        return this;
    }

    public GeneratorUtil withClassModifyingPlugin(final ClassModifyingPlugin classModifyingPlugin) {
        this.classModifyingPlugins.add(classModifyingPlugin);
        return this;
    }

    public GeneratorUtil withTypeModifyingPlugin(final TypeModifyingPlugin typeModifyingPlugin) {
        this.typeModifyingPlugins.add(typeModifyingPlugin);
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

        final Schema schema = schemaResolver.resolve(new Resource(get(""),
                jsonSchemaFile.toPath(),
                new FileResourceLoader(),
                inputStreamToJsonObjectConverter));

        classModifyingPlugins.add(newAddFieldsAndMethodsToClassPlugin());

        final PluginProvider pluginProvider = new ModifyingPluginProvider(
                classModifyingPlugins,
                typeModifyingPlugins);

        final TypeNameProvider typeNameProvider = new TypeNameProvider(generationContext, new PackageNameParser(), new ClassNameParser());
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
                generatorProperties,
                serialVersionUIDGenerator.generateHashCode(schema));

        final Visitable visitableSchema = visitableFactory.createWith(ROOT_FIELD_NAME, schema, acceptorService);

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
        final Schema schema = schemaResolver.resolve(new Resource(get(""),
                schemaFile.toPath(),
                new FileResourceLoader(),
                inputStreamToJsonObjectConverter));
        schema.validate(new JSONObject(json));
    }

    private Class<?> writeAndCompile(final OutputDirectories outputDirectories, final GenerationContext generationContext, final SourceWriter sourceWriter, final ClassCompiler classCompiler, final ClassGeneratable classGeneratable) {
        sourceWriter.write(classGeneratable, generationContext);
        return classCompiler.compile(classGeneratable, generationContext, outputDirectories.getClassesOutputDirectory().toFile());
    }
}
