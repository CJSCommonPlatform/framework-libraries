package uk.gov.justice.generation.pojo.integration.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_INTEGER_FOR_INTS;
import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BigIntegerIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer()
            .objectMapper()
            .enable(USE_BIG_INTEGER_FOR_INTS);
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/custom-return-type");
    }

    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo.custom.type";

        final File jsonSchemaFile = new File("src/test/resources/schemas/custom-return-type.json");

        final List<Class<?>> classes = generatorUtil
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        final String firstName = "Frederick";
        final String lastName = "Bloggs";
        final BigInteger salary = new BigInteger("1000000");

        final Class<?> employeeClass = classes.get(0);

        final Object employee = classInstantiator.newInstance(
                employeeClass,
                firstName,
                lastName,
                salary);

        final String employeeJson = objectMapper.writeValueAsString(employee);

        with(employeeJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.salary", is(salary.intValue()))
        ;

        generatorUtil.validate(jsonSchemaFile, employeeJson);
    }
}
