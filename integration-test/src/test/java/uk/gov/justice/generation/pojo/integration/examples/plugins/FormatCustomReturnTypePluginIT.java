package uk.gov.justice.generation.pojo.integration.examples.plugins;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;
import static uk.gov.justice.generation.pojo.plugin.typemodifying.FormatCustomReturnTypePlugin.formatCustomReturnTypePlugin;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class FormatCustomReturnTypePluginIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new File("src/test/resources/schemas/examples/plugins/format-custom-return-types-plugin.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/plugins/format-custom-return-types-plugin");
    }

    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo.formatcustomreturntypes";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("EmployeeWithFormatCustomReturnType")
                .addFormatTypeMappingOf("date-time", "java.time.ZonedDateTime")
                .build();

        final List<Class<?>> classes = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .withTypeModifyingPlugin(formatCustomReturnTypePlugin())
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
                        packageName,
                        outputDirectories);

        final String firstName = "firstName";
        final String lastName = "lastName";
        final ZonedDateTime startDate = ZonedDateTimes.fromString("2016-03-18T00:46:54.700Z");

        final Class<?> employeeClass = classes.get(0);

        final Object employee = classInstantiator.newInstance(
                employeeClass,
                firstName,
                lastName,
                startDate);

        final String employeeJson = objectMapper.writeValueAsString(employee);

        with(employeeJson)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.startDate", is("2016-03-18T00:46:54.700Z"))
        ;

        generatorUtil.validate(JSON_SCHEMA_FILE, employeeJson);
    }
}
