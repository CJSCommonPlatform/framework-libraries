package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class DefinitionBuilderIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

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

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldBuildTypeSpecFromSchema() throws Exception {
        final File schemaFile = new File("src/test/resources/schemas/person-schema.json");
        final String packageName = "uk.gov.justice.pojo.definition.builder";
        final Schema schema = schemaLoader.loadFrom(schemaFile);

        final List<Class<?>> classes = generatorUtil.generateAndCompileJavaSource(
                schemaFile,
                packageName,
                schema,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath());

        final Class<?> personClass = classes.get(0);

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
