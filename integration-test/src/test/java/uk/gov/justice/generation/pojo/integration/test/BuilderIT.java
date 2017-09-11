package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class BuilderIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final OutputDirectories outputDirectories = new OutputDirectories();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/builder");
    }

    @Test
    public void shouldGenerateAClassWithAMapForAdditionalPropertiesIfAdditionalPropertiesIsTrue() throws Exception {
        final File jsonSchemaFile = new File("src/test/resources/schemas/student.json");
        final String packageName = "uk.gov.justice.pojo.builder.student";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

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

        generatorUtil.validate(jsonSchemaFile, json);
    }
}
