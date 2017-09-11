package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class DefinitionBuilderIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/definition-builder");
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldBuildTypeSpecFromSchema() throws Exception {
        final File schemaFile = new File("src/test/resources/schemas/person-schema.json");
        final String packageName = "uk.gov.justice.pojo.definition.builder";

        final List<Class<?>> classes = generatorUtil.generateAndCompileJavaSource(
                schemaFile,
                packageName,
                outputDirectories);

        final Class<?> personClass = classes.get(0);

        final String lastName = "lastName";
        final String firstName = "firstName";
        final Boolean required = true;
        final Integer signedInCount = 25;
        final BigDecimal ratio = BigDecimal.valueOf(2.5);

        final Object person = classInstantiator.newInstance(personClass, firstName, lastName, ratio, required, signedInCount);

        final String personJson = objectMapper.writeValueAsString(person);

        with(personJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.required", is(required))
                .assertThat("$.signedInCount", is(signedInCount))
                .assertThat("$.ratio", is(ratio.doubleValue()))
        ;

        generatorUtil.validate(schemaFile, personJson);
    }
}
