package uk.gov.justice.generation.pojo.integration.test;

import static java.util.Collections.emptyList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorFactoryBuilder;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.write.SourceWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Before;
import org.junit.Test;

public class EmptySchemaIT {

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
        sourceOutputDirectory = new File("./target/test-generation/empty-schemas");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldGenerateAnEmptyClassWithAdditionalPropertiesIfNoAdditionalPropertiesSpecified() throws Exception {

        final List<Class<?>> classes = setupAndGenerate("empty-schema.json");

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        assertThat(emptySchemaClass.getDeclaredField("additionalProperties"), is(notNullValue()));
        assertThat(emptySchemaClass.getDeclaredField("additionalProperties").getType().getName(), is("java.util.Map"));

        assertThat(emptySchemaClass.getDeclaredMethod("getAdditionalProperties"), is(notNullValue()));
        assertThat(emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class), is(notNullValue()));

        assertThat(emptySchemaClass.getAnnotation(Event.class), is(notNullValue()));
    }

    @Test
    public void shouldGenerateAnEmptyClassWithAdditionalPropertiesIfoAdditionalPropertiesIsSetToTrue() throws Exception {

        final List<Class<?>> classes = setupAndGenerate("empty-schema-with-additional-properties-true.json");

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        emptySchemaClass.getDeclaredField("additionalProperties");
        emptySchemaClass.getDeclaredField("additionalProperties").getType().getName();
        emptySchemaClass.getDeclaredMethod("getAdditionalProperties");
        emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);

        assertThat(emptySchemaClass.getAnnotation(Event.class), is(notNullValue()));
    }

    @Test
    public void shouldGenerateAnEmptyClassWithAdditionalPropertiesIfoAdditionalPropertiesIsSetToFalse() throws Exception {

        final List<Class<?>> classes = setupAndGenerate("empty-schema-with-additional-properties-false.json");

        assertThat(classes.size(), is(1));

        final Class<?> emptySchemaClass = classes.get(0);

        try {
            emptySchemaClass.getDeclaredField("additionalProperties");
            fail();
        } catch (final NoSuchFieldException ignored) {
        }
        try {
            emptySchemaClass.getDeclaredField("additionalProperties").getType().getName();
            fail();
        } catch (final NoSuchFieldException ignored) {
        }
        try {
            emptySchemaClass.getDeclaredMethod("getAdditionalProperties");
            fail();
        } catch (final NoSuchMethodException ignored) {
        }
        try {
            emptySchemaClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
            fail();
        } catch (final NoSuchMethodException ignored) {
        }

        assertThat(emptySchemaClass.getAnnotation(Event.class), is(notNullValue()));
    }

    private List<Class<?>> setupAndGenerate(final String fileName) {
        final File jsonSchemaFile = new File("src/test/resources/schemas/" + fileName);
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.rootFieldNameFrom(jsonSchemaFile);
        final String packageName = "uk.gov.justice.pojo.empty.schemas";
        final GenerationContext generationContext = new GenerationContext(
                sourceOutputDirectory.toPath(),
                packageName,
                jsonSchemaFile.getName(),
                emptyList());

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
        final VisitableFactory visitableSchemaFactory = new VisitableFactory();
        final Visitable visitableSchema = visitableSchemaFactory.createWith(fieldName, schema, new DefaultAcceptorService(visitableSchemaFactory));

        visitableSchema.accept(definitionBuilderVisitor);

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

        return newClasses;
    }
}
