package uk.gov.justice.generation.pojo.integration.test;

import static java.util.Collections.emptyList;
import static org.apache.commons.io.FileUtils.cleanDirectory;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorFactoryBuilder;
import uk.gov.justice.generation.pojo.visitable.VisitableSchema;
import uk.gov.justice.generation.pojo.visitable.VisitableSchemaFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorFactory;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.write.SourceWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Before;
import org.junit.Test;

public class DodgySchemaIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final NameGenerator rootFieldNameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final DefaultDefinitionFactory definitionFactory = new DefaultDefinitionFactory();
    private final GeneratorFactoryBuilder generatorFactoryBuilder = new GeneratorFactoryBuilder();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/dodgy-schemas");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldName() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/null-schema.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.rootFieldNameFrom(jsonSchemaFile);
        final String packageName = "uk.gov.justice.pojo.dodgy.schemas";
        final GenerationContext generationContext = new GenerationContext(
                sourceOutputDirectory.toPath(),
                packageName,
                jsonSchemaFile.getName(),
                emptyList());

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
        final VisitableSchemaFactory visitableSchemaFactory = new VisitableSchemaFactory();
        final VisitableSchema visitableSchema = visitableSchemaFactory.createWith(schema, new DefaultAcceptorFactory(visitableSchemaFactory));

        visitableSchema.accept(fieldName, definitionBuilderVisitor);

        final List<Class<?>> newClasses = new ArrayList<>();

        final PluginProvider pluginProvider = new DefaultPluginProvider();

        final JavaGeneratorFactory javaGeneratorFactory = generatorFactoryBuilder
                .withGenerationContext(generationContext)
                .withPluginProvider(pluginProvider)
                .build();

        javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor.getDefinitions(), pluginProvider, generationContext)
                .forEach(classGeneratable -> {
                    sourceWriter.write(classGeneratable, generationContext);
                    final Class<?> newClass = classCompiler.compile(classGeneratable, generationContext, classesOutputDirectory);
                    newClasses.add(newClass);
                });

    }
}
