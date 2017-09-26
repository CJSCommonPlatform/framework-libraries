package uk.gov.justice.generation.pojo.integration.test;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomReturnTypesIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/custom-return-types");
    }

    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo.customreturntypes";

        final File jsonSchemaFile = new File("src/test/resources/schemas/custom-return-types.json");

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withTypeMappings(of(
                        "reference.uuid", "java.util.UUID",
                        "reference.bigInteger", "java.math.BigInteger",
                        "reference.date", "java.time.ZonedDateTime"
                ))
                .build();

        final List<Class<?>> classes = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        final UUID employeeId = randomUUID();
        final String firstName = "firstName";
        final String lastName = "lastName";
        final BigInteger salary = new BigInteger("1000000000");
        final ZonedDateTime startDate = ZonedDateTimes.fromString("2016-03-18T00:46:54.700Z");

        final Class<?> employeeClass = classes.get(0);

        final Object employee = classInstantiator.newInstance(
                employeeClass,
                employeeId,
                firstName,
                lastName,
                salary,
                startDate);

        final String employeeJson = objectMapper.writeValueAsString(employee);

        System.out.println(employeeJson);

        with(employeeJson)
                .assertThat("$.employeeId", is(employeeId.toString()))
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.salary", is(salary.intValue()))
                .assertThat("$.startDate", is("2016-03-18T00:46:54.700Z"))
        ;

        generatorUtil.validate(jsonSchemaFile, employeeJson);
    }
}
