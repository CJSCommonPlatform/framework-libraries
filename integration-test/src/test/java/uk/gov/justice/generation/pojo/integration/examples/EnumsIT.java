package uk.gov.justice.generation.pojo.integration.examples;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

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

        final Optional<Method> valueForMethod = ReflectionUtil.methodOf(enumClass, "valueFor");
        assertThat(valueForMethod.isPresent(), is(true));

        final Optional red = (Optional) valueForMethod.get().invoke(null, "Red");
        assertThat(red.isPresent(), is(true));

        final String name = "Fred";
        final Integer age = 21;
        final Object personWithEnum = classInstantiator.newInstance(personWithEnumClass, age, red.get(), name);

        final String studentJson = objectMapper.writeValueAsString(personWithEnum);

        with(studentJson)
                .assertThat("$.name", is(name))
                .assertThat("$.age", is(age))
                .assertThat("$.favouriteColour", is("Red"))
        ;

        generatorUtil.validate(JSON_SCHEMA_FILE, studentJson);
    }
}
