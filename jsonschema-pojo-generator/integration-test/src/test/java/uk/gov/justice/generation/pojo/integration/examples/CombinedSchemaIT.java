package uk.gov.justice.generation.pojo.integration.examples;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.NullParameter;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddEventAnnotationToClassPlugin;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class CombinedSchemaIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new ClasspathFileResource().getFileFromClasspath("/schemas/examples/combined-schema.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/combined-schema");
    }

    @Test
    public void shouldGeneratePojoFromSchemaDocumentWithCombinedSchema() throws Exception {

        final String packageName = "uk.gov.justice.pojo.combined.schema";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("AddressFromCombinedSchema")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withClassModifyingPlugin(new AddEventAnnotationToClassPlugin())
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(4));
        final Class<?> ukCommsClass = newClasses.get(0);
        final Class<?> usCommsClass = newClasses.get(1);
        final Class<?> communicationClass = newClasses.get(2);
        final Class<?> addressClass = newClasses.get(3);

        assertThat(ukCommsClass.getSimpleName(), is("UkComms"));
        assertThat(usCommsClass.getSimpleName(), is("UsComms"));
        assertThat(communicationClass.getSimpleName(), is("Communication"));
        assertThat(addressClass.getSimpleName(), is("AddressFromCombinedSchema"));

        assertThat(addressClass.getAnnotation(Event.class), is(notNullValue()));

        final Object ukComms = classInstantiator.newInstance(ukCommsClass, true);
        final Object usComms = classInstantiator.newInstance(usCommsClass, true);
        final Object communication = classInstantiator.newInstance(communicationClass, true);

        final String city = "city";
        final String county = "county";
        final String addressLine1 = "addressLine1";
        final String addressLine2 = "addressLine2";
        final String postCode = "postCode";

        final NullParameter nullZipCode = new NullParameter(String.class);
        final NullParameter nullState = new NullParameter(String.class);

        final Object addressObject = classInstantiator.newInstance(
                addressClass,
                addressLine1,
                addressLine2,
                city,
                communication,
                nullState,
                usComms,
                nullZipCode,
                county,
                postCode,
                ukComms
        );

        final String json = objectMapper.writeValueAsString(addressObject);

        with(json)
                .assertThat("$.addressLine1", is("addressLine1"))
                .assertThat("$.addressLine2", is("addressLine2"))
                .assertThat("$.city", is("city"))
                .assertThat("$.county", is("county"))
                .assertThat("$.postCode", is("postCode"))
                .assertNotDefined("$.state")
                .assertNotDefined("$.zipCode")
                .assertThat("$.communication.telephone", is(true))
                .assertThat("$.usComms.telephone", is(true))
                .assertThat("$.ukComms.telephone", is(true))
        ;

        generatorUtil.validate(JSON_SCHEMA_FILE, json);
    }
}
