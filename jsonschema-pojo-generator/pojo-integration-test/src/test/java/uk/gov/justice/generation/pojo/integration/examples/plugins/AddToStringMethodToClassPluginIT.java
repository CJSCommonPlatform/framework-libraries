package uk.gov.justice.generation.pojo.integration.examples.plugins;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddToStringMethodToClassPlugin.newAddToStringMethodToClassPlugin;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddToStringMethodToClassPluginIT {

    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @BeforeEach
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/plugins/to/string");
    }

    @Test
    public void shouldAddToStringMethodUsingAllFields() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/examples/plugins/add-to-string-method-to-class.json");
        final String packageName = "uk.gov.justice.pojo.string";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("StudentWithToString")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withClassModifyingPlugin(newAddToStringMethodToClassPlugin())
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Class<?> studentClass = newClasses.get(0);

        final Object student = classInstantiator.newInstance(studentClass, 23, "Fred", "Bloggs");

        assertThat(student.toString(), is("StudentWithToString{age='23',firstName='Fred',lastName='Bloggs'}"));
    }
}
