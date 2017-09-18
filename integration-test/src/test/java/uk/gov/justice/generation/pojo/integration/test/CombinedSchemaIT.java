package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.NullParameter;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddEventAnnotationToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddHashcodeAndEqualsPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddToStringMethodToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.BuilderGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

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

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/combined-schema");
    }

    @Test
    public void shouldParseAVeryComplexSchemaDocument() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/address.json");
        final String packageName = "uk.gov.justice.pojo.combined.schema";

        final List<ClassModifyingPlugin> classModifyingPlugins = asList(
                new AddAdditionalPropertiesToClassPlugin(),
                new AddEventAnnotationToClassPlugin(),
                new AddFieldsAndMethodsToClassPlugin(),
                new AddHashcodeAndEqualsPlugin(new AdditionalPropertiesDeterminer()),
                new AddToStringMethodToClassPlugin(new AdditionalPropertiesDeterminer()),
                new GenerateBuilderForClassPlugin(new BuilderGeneratorFactory()),
                new MakeClassSerializablePlugin()
        );

        final List<Class<?>> newClasses = generatorUtil
                .withClassModifyingPlugins(classModifyingPlugins)
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
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
        assertThat(addressClass.getSimpleName(), is("Address"));

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

        generatorUtil.validate(jsonSchemaFile, json);
    }
}
