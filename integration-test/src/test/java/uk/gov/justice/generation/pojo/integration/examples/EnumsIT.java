package uk.gov.justice.generation.pojo.integration.examples;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class EnumsIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new File("src/test/resources/schemas/examples/enum.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/enums");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo.enums";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("PersonWithEnum")
                .build();

        final List<Class<?>> classes = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
                        packageName,
                        outputDirectories);

        final Class<?> personWithEnumClass = classes.get(1);

        final Class<? extends Enum> enumClass = (Class<? extends Enum>) classes.get(0);
        final Enum<?> red = Enum.valueOf(enumClass, "RED");

        final String name = "Fred";
        final Integer age = 21;
        final Object personWithEnum = classInstantiator.newInstance(personWithEnumClass, age, red, name);

        final String studentJson = objectMapper.writeValueAsString(personWithEnum);

        with(studentJson)
                .assertThat("$.name", is(name))
                .assertThat("$.age", is(age))
                .assertThat("$.favouriteColour", is("Red"))
        ;

        generatorUtil.validate(JSON_SCHEMA_FILE, studentJson);
    }
}
