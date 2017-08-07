package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.core.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.core.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.core.SourceWriter;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassGenerator;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.integration.utils.JsonSchemaLoader;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.ObjectSchema;
import org.junit.Test;

public class DefintionBuilderIntegrationTest {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    @Test
    public void shouldBuildTypeSpecFromSchema() throws Exception {
        final ObjectSchema schema = (ObjectSchema) JsonSchemaLoader
                .loadSchema("src/test/resources/schemas/person-schema.json");

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo");
        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(schema);

        jsonSchemaWrapper.accept(definitionBuilderVisitor);

        final ClassDefinition personClassDefinition = definitionBuilderVisitor.getDefinitions().get(0);
        final ClassGenerator personClassGenerator = new ClassGenerator(personClassDefinition);


        final File sourceOutputDirectory = new File("./target/test-generation");
        final File classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.delete();

        sourceWriter.write(personClassGenerator, sourceOutputDirectory);
        final Class<?> personClass = classCompiler.compile(personClassGenerator, sourceOutputDirectory, classesOutputDirectory);

        assertThat(personClass.getName(), is(personClassDefinition.getClassName().getFullyQualifiedName()));

        final String lastName = "lastName";
        final String firstName = "firstName";

        final Constructor<?> personConstructor = personClass.getConstructor(String.class, String.class);

        final Object person = personConstructor.newInstance(firstName, lastName);

        final String personJson = objectMapper.writeValueAsString(person);

        with(personJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
        ;
    }
}
