package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.ClassNameProvider;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
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
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class DefinitionBuilderIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final NameGenerator nameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final DefaultDefinitionFactory definitionFactory = new DefaultDefinitionFactory(new ClassNameProvider());

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/definition-builder");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldBuildTypeSpecFromSchema() throws Exception {
        final File schemaFile = new File("src/test/resources/schemas/person-schema.json");
        final Schema schema = schemaLoader.loadFrom(schemaFile);
        final String fieldName = nameGenerator.rootFieldNameFrom(schemaFile);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo.definition.builder", definitionFactory);

        final VisitableSchemaFactory visitableSchemaFactory = new VisitableSchemaFactory();
        final VisitableSchema visitableSchema = visitableSchemaFactory.createWith(schema, new DefaultAcceptorFactory(visitableSchemaFactory));

        visitableSchema.accept(fieldName, definitionBuilderVisitor);

        final ClassDefinition personClassDefinition = (ClassDefinition) definitionBuilderVisitor.getDefinitions().get(0);

        final ClassGeneratable personClassGenerator = new JavaGeneratorFactory()
                .createClassGeneratorsFor(singletonList(personClassDefinition), new DefaultPluginProvider())
                .get(0);

        sourceWriter.write(personClassGenerator, sourceOutputDirectory.toPath());
        final Class<?> personClass = classCompiler.compile(personClassGenerator, sourceOutputDirectory, classesOutputDirectory);

        assertThat(personClass.getName(), is(personClassDefinition.getClassName().getFullyQualifiedName()));

        final String lastName = "lastName";
        final String firstName = "firstName";
        final Boolean required = true;
        final Integer signedInCount = 25;
        final BigDecimal ratio = BigDecimal.valueOf(2.5);

        final Constructor<?> personConstructor = personClass.getConstructor(String.class, String.class, BigDecimal.class, Boolean.class, Integer.class);

        final Object person = personConstructor.newInstance(firstName, lastName, ratio, required, signedInCount);

        final String personJson = objectMapper.writeValueAsString(person);

        with(personJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.required", is(required))
                .assertThat("$.signedInCount", is(signedInCount))
                .assertThat("$.ratio", is(ratio.doubleValue()))
        ;

        schema.validate(new JSONObject(personJson));
    }
}
