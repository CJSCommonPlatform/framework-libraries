package uk.gov.justice.generation.pojo.integration.examples.plugins;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddHashcodeAndEqualsPlugin.newAddHashcodeAndEqualsPlugin;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AddHashcodeAndEqualsPluginIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new File("src/test/resources/schemas/examples/plugins/add-hashcode-and-equals-plugin.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/plugins/hashcode-and-equals");
    }

    @Test
    public void shouldCreateAnObjectWithHashcodeAndEqualsMethods() throws Exception {

        final String packageName = "uk.gov.justice.pojo.equals";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("PersonWithHashCodeAndEquals")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .withClassModifyingPlugin(newAddHashcodeAndEqualsPlugin())
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Object hashcodeAndEquals_1 = classInstantiator.newInstance(newClasses.get(0), "Fred", "Bloggs");
        final Object hashcodeAndEquals_2 = classInstantiator.newInstance(newClasses.get(0), "Mildred", "Bloggs");
        final Object hashcodeAndEquals_3 = classInstantiator.newInstance(newClasses.get(0), "Fred", "Bloggs");

        assertThat(hashcodeAndEquals_1, is(hashcodeAndEquals_1));
        assertThat(hashcodeAndEquals_1, is(hashcodeAndEquals_3));
        assertThat(hashcodeAndEquals_1, is(not(hashcodeAndEquals_2)));
    }
}
