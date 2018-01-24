package uk.gov.justice.generation.pojo.integration.examples.plugins;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin.newGenerateBuilderForClassPlugin;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class GenerateBuilderForClassPluginWithAdditionalPropertiesIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final OutputDirectories outputDirectories = new OutputDirectories();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private static final File JSON_SCHEMA_FILE = new ClasspathFileResource().getFileFromClasspath("/schemas/examples/plugins/generate-builder-for-class-with-additional-properties.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/plugins/generate-builder-for-class-plugin-with-additional-properties");
    }

    @Test
    public void shouldGenerateClassWithMapForAdditionalPropertiesIfAdditionalPropertiesIsTrue() throws Exception {
        final String packageName = "uk.gov.justice.pojo.builder";


        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("StudentPojoWithBuilder")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .withClassModifyingPlugin(newGenerateBuilderForClassPlugin())
                .withClassModifyingPlugin(new AddAdditionalPropertiesToClassPlugin())
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Class<?> studentClass = newClasses.get(0);

        final Object studentBuilder = studentClass.getMethod("studentPojoWithBuilder").invoke(null);

        assertThat(studentBuilder.getClass().getName(), is("uk.gov.justice.events.pojo.builder.StudentPojoWithBuilder$Builder"));

        final String firstName = "Molly";
        final String lastName = "O'Golly";
        final Integer age = 23;
        final String haircut = "dreads";
        final String additionalPropertiesName = "extra_property_name";
        final Object additionalPropertiesValue = "extra_property_value";

        studentBuilder.getClass().getMethod("withFirstName", String.class).invoke(studentBuilder, firstName);
        studentBuilder.getClass().getMethod("withLastName", String.class).invoke(studentBuilder, lastName);
        studentBuilder.getClass().getMethod("withAge", Integer.class).invoke(studentBuilder, age);
        studentBuilder.getClass().getMethod("withHaircut", String.class).invoke(studentBuilder, haircut);
        studentBuilder.getClass().getMethod("withAdditionalProperty", String.class, Object.class).invoke(studentBuilder, additionalPropertiesName, additionalPropertiesValue);

        final Object student = studentBuilder.getClass().getMethod("build").invoke(studentBuilder);

        final String json = objectMapper.writeValueAsString(student);

        with(json)
                .assertThat("$.firstName", is(firstName))
                .assertThat("$.lastName", is(lastName))
                .assertThat("$.age", is(age))
                .assertThat("$.haircut", is(haircut))
                .assertThat("$.extra_property_name", is(additionalPropertiesValue))
        ;

        generatorUtil.validate(JSON_SCHEMA_FILE, json);
    }
}
