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
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class VanillaPojoIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    private static final File JSON_SCHEMA_FILE = new ClasspathFileResource().getFileFromClasspath("/schemas/examples/vanilla-pojo.json");

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/vanilla");
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldGeneratePojoFromSchemaDocumentWithArrayOfItems() throws Exception {

        final String packageName = "uk.gov.justice.pojo.vanilla";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("Teen")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        JSON_SCHEMA_FILE,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(1));

        final Class<?> teenClass = newClasses.get(0);

        assertThat(teenClass.getSimpleName(), is("Teen"));

        final String name = "Rebecca Basic";
        final Integer age = 13;
        final BigDecimal pocketMoney = new BigDecimal("4.75");
        final Boolean likesJustinBieber = true;

        final Object teen = classInstantiator.newInstance(
                teenClass,
                age,
                likesJustinBieber,
                name,
                pocketMoney
        );

        final String json = objectMapper.writeValueAsString(teen);

        with(json)
                .assertThat("$.name", is(name))
                .assertThat("$.age", is(age))
                .assertThat("$.likesJustinBieber", is(likesJustinBieber))
                .assertThat("$.pocketMoney", is(pocketMoney.doubleValue()))
        ;

        generatorUtil.validate(JSON_SCHEMA_FILE, json);
    }
}
