package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Collections.emptyList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class BuilderIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final NameGenerator rootFieldNameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final DefaultDefinitionFactory definitionFactory = new DefaultDefinitionFactory();
    private final GeneratorFactoryBuilder generatorFactoryBuilder = new GeneratorFactoryBuilder();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/builder");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldGenerateAClassWithAMapForAdditionalPropertiesIfAdditionalPropertiesIsTrue() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/student.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.rootFieldNameFrom(jsonSchemaFile);
        final String packageName = "uk.gov.justice.pojo.builder.student";
        final GenerationContext generationContext = new GenerationContext(
                sourceOutputDirectory.toPath(),
                packageName,
                jsonSchemaFile.getName(),
                emptyList());

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
        final VisitableFactory visitableFactory = new VisitableFactory();
        final Visitable visitableSchema = visitableFactory.createWith(fieldName, schema, new DefaultAcceptorService(visitableFactory));

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

        assertThat(newClasses.size(), is(1));

        final Class<?> studentClass = newClasses.get(0);

        final Object studentBuilder = studentClass.getMethod("student").invoke(null);

        assertThat(studentBuilder.getClass().getName(), is("uk.gov.justice.pojo.builder.student.Student$Builder"));

        final String firstName = "Molly";
        final String lastName = "O'Golly";
        final Integer age = 23;
        final String haircut = "dreads";

        studentBuilder.getClass().getMethod("withFirstName", String.class).invoke(studentBuilder, firstName);
        studentBuilder.getClass().getMethod("withLastName", String.class).invoke(studentBuilder, lastName);
        studentBuilder.getClass().getMethod("withAge", Integer.class).invoke(studentBuilder, age);
        studentBuilder.getClass().getMethod("withHaircut", String.class).invoke(studentBuilder, haircut);

        final Object student = studentBuilder.getClass().getMethod("build").invoke(studentBuilder);

        final String json = objectMapper.writeValueAsString(student);

        with(json)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.age", is(age))
                .assertThat("$.haircut", is(haircut))
        ;

        schema.validate(new JSONObject(json));
    }
}
