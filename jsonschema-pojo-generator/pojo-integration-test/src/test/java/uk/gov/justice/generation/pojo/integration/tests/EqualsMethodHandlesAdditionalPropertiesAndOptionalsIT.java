package uk.gov.justice.generation.pojo.integration.tests;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddHashcodeAndEqualsPlugin.newAddHashcodeAndEqualsPlugin;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class EqualsMethodHandlesAdditionalPropertiesAndOptionalsIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new ClasspathFileResource().getFileFromClasspath("/schemas/tests/equals-method-handles-additional-properties.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/tests/equals-method-handles-additional-propertie");
    }

    @Test
    public void shouldHandleOptionalsAndAdditionalPropertiesInTheClass() throws Exception {

        final String packageName = "uk.gov.justice.pojo.hashcode";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("Person")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withClassModifyingPlugin(new AddAdditionalPropertiesToClassPlugin())
                .withClassModifyingPlugin(newAddHashcodeAndEqualsPlugin())
                .withTypeModifyingPlugin(new SupportJavaOptionalsPlugin())
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Optional<String> firstName = of("Fred");
        final String lastName = "Bloggs";

        final Class<?> hashcodeAndEqualsClass = newClasses.get(0);
        final Object hashcodeAndEquals_1 = classInstantiator.newInstance(hashcodeAndEqualsClass, firstName, lastName, new HashMap<>());
        final Object hashcodeAndEquals_2 = classInstantiator.newInstance(hashcodeAndEqualsClass, firstName, lastName, new HashMap<>());

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
