package uk.gov.justice.generation.pojo.integration.test;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class HashcodeAndEqualsIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/hashcode-and-equals");
    }

    @Test
    public void shouldCreateAnObjectWithHashcodeAndEqualsMethods() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/hashcode-and-equals.json");
        final String packageName = "uk.gov.justice.pojo.equals";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

        assertThat(newClasses.size(), is(1));


        final Optional<String> firstName = of("Fred");
        final String lastName = "Bloggs";

        final Object hashcodeAndEquals_1 = classInstantiator.newInstance(newClasses.get(0), firstName, lastName);
        final Object hashcodeAndEquals_2 = classInstantiator.newInstance(newClasses.get(0), empty(), lastName);
        final Object hashcodeAndEquals_3 = classInstantiator.newInstance(newClasses.get(0), firstName, lastName);

        assertThat(hashcodeAndEquals_1, is(hashcodeAndEquals_1));
        assertThat(hashcodeAndEquals_1, is(hashcodeAndEquals_3));
        assertThat(hashcodeAndEquals_1, is(not(hashcodeAndEquals_2)));
    }

    @Test
    public void shouldHandleAdditionalPropertiesInTheClass() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/hashcode-and-equals.json");
        final String packageName = "uk.gov.justice.pojo.hashcode";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Optional<String> firstName = of("Fred");
        final String lastName = "Bloggs";

        final Class<?> hashcodeAndEqualsClass = newClasses.get(0);
        final Object hashcodeAndEquals_1 = classInstantiator.newInstance(hashcodeAndEqualsClass, firstName, lastName);
        final Object hashcodeAndEquals_2 = classInstantiator.newInstance(hashcodeAndEqualsClass, firstName, lastName);

        assertThat(hashcodeAndEquals_1, is(hashcodeAndEquals_2));

        hashcodeAndEqualsClass
                .getDeclaredMethod("setAdditionalProperty", String.class, Object.class)
                .invoke(hashcodeAndEquals_1, "additionalPropertyName", "additionalPropertyValue");

        assertThat(hashcodeAndEquals_1, is(not(hashcodeAndEquals_2)));

        hashcodeAndEqualsClass
                .getDeclaredMethod("setAdditionalProperty", String.class, Object.class)
                .invoke(hashcodeAndEquals_2, "additionalPropertyName", "additionalPropertyValue");

        assertThat(hashcodeAndEquals_1, is(hashcodeAndEquals_2));
    }
}
