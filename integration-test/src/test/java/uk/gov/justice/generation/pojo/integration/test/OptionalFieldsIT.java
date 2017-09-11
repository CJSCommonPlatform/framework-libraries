package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class OptionalFieldsIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/optional-schema");
    }

    @Test
    public void shouldParseAVeryComplexSchemaDocument() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/person-schema-optional.json");
        final String packageName = "uk.gov.justice.pojo.optional.schema";

        final List<Class<?>> classes = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

        assertThat(classes.size(), is(1));

        final Class<?> personClass = classes.get(0);
        final String firstName = "Fred";
        final String lastName = "Bloggs";
        final Optional<String> favouriteFood = of("Curry and Chips");
        final Optional<String> hairStyle = empty();


        final Constructor<?> personConstructor = personClass.getConstructor(Optional.class, String.class, Optional.class, String.class);

        final Object person = personConstructor.newInstance(favouriteFood, firstName, hairStyle, lastName);

        final String json = objectMapper.writeValueAsString(person);

        with(json)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.favouriteFood", is("Curry and Chips"))
                .assertNotDefined("$.hairStyle")
        ;

        generatorUtil.validate(jsonSchemaFile, json);
    }
}
