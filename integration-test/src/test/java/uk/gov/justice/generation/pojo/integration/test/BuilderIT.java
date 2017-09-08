package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class BuilderIT {

    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

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
        final String packageName = "uk.gov.justice.pojo.builder.student";
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                schema,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath());

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
