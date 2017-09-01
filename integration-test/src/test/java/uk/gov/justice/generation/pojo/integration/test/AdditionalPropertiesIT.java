package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.ClassNameProvider;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.visitable.VisitableSchema;
import uk.gov.justice.generation.pojo.visitable.VisitableSchemaFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorFactory;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.junit.Before;
import org.junit.Test;

public class AdditionalPropertiesIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final NameGenerator rootFieldNameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final DefaultDefinitionFactory definitionFactory = new DefaultDefinitionFactory(new ClassNameProvider());

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/additional-properties");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldName() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/additional-properties.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.rootFieldNameFrom(jsonSchemaFile);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo.additional.properties", definitionFactory);
        final VisitableSchemaFactory visitableSchemaFactory = new VisitableSchemaFactory();
        final VisitableSchema visitableSchema = visitableSchemaFactory.createWith(schema, new DefaultAcceptorFactory(visitableSchemaFactory));

        visitableSchema.accept(fieldName, definitionBuilderVisitor);

        final List<Class<?>> newClasses = new ArrayList<>();

        javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor.getDefinitions(), new DefaultPluginProvider())
                .forEach(classGeneratable -> {
                    sourceWriter.write(classGeneratable, sourceOutputDirectory.toPath());
                    final Class<?> newClass = classCompiler.compile(classGeneratable, sourceOutputDirectory, classesOutputDirectory);
                    newClasses.add(newClass);
                });

        assertThat(newClasses.size(), is(1));

        final Class<?> additionalPropertiesClass = newClasses.get(0);

        final String firstName = "firstName";
        final String lastName = "lastName";

        final Constructor<?> additionalPropertiesConstructor = additionalPropertiesClass
                .getConstructor(String.class, String.class);

        final Object additionalProperties = additionalPropertiesConstructor.newInstance(firstName, lastName);
        final Method additionalPropertySetter = additionalPropertiesClass.getDeclaredMethod("setAdditionalProperty", String.class, Object.class);
        additionalPropertySetter.invoke(additionalProperties, "additionalPropertyName", "additionalPropertyValue");

        final String json = objectMapper.writeValueAsString(additionalProperties);

        with(json)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.additionalPropertyName", is("additionalPropertyValue"))
        ;
    }
}
