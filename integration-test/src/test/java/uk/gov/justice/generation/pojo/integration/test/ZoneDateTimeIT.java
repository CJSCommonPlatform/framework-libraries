package uk.gov.justice.generation.pojo.integration.test;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZoneDateTimeIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/class-generator");
    }

    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo.classgenerator";

        final File jsonSchemaFile = new File("src/test/resources/schemas/employee-with-date.json");

        final Map<String, String> generatorProperties = of(
                "typemapping.reference.date", "java.time.ZonedDateTime"
        );

        final List<Class<?>> classes = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        final String firstName = "firstName";
        final String lastName = "lastName";
        final BigDecimal poundsPerHour = new BigDecimal("5.99");
        final ZonedDateTime startDate = ZonedDateTimes.fromString("2016-03-18T00:46:54.700Z");

        final String firstLine = "firstLine";
        final String postCode = "postCode";

        final Class<?> addressClass = classes.get(0);
        final Class<?> employeeClass = classes.get(1);

        final Object employee = classInstantiator.newInstance(
                employeeClass,
                classInstantiator.newInstance(addressClass, firstLine, postCode),
                firstName,
                lastName,
                poundsPerHour,
                startDate);

        final String employeeJson = objectMapper.writeValueAsString(employee);

        with(employeeJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.poundsPerHour", is(poundsPerHour.doubleValue()))
                .assertThat("$.startDate", is("2016-03-18T00:46:54.700Z"))
                .assertThat("$.address.firstLine", is(firstLine))
                .assertThat("$.address.postCode", is(postCode))
        ;

        generatorUtil.validate(jsonSchemaFile, employeeJson);
    }
}
