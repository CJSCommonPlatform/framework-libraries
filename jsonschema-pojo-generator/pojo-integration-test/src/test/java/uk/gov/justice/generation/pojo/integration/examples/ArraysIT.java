package uk.gov.justice.generation.pojo.integration.examples;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.pojo.integration.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArraysIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @BeforeEach
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/examples/arrays");
    }

    @Test
    public void shouldGeneratePojoFromSchemaDocumentWithArrayOfItems() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource().getFileFromClasspath("/schemas/examples/array.json");
        final String packageName = "uk.gov.justice.pojo.arrays";

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName("RecipeWithArray")
                .build();

        final List<Class<?>> newClasses = generatorUtil
                .withGeneratorProperties(generatorProperties)
                .generateAndCompileJavaSource(
                        jsonSchemaFile,
                        packageName,
                        outputDirectories);

        assertThat(newClasses.size(), is(2));

        final Class<?> ingredientsClass = newClasses.get(0);
        final Class<?> recipeAddedClass = newClasses.get(1);

        assertThat(recipeAddedClass.getSimpleName(), is("RecipeWithArray"));
        assertThat(ingredientsClass.getSimpleName(), is("Ingredient"));

        final Object ingredient_1 = classInstantiator.newInstance(ingredientsClass, "Eye of Newt", 1);
        final Object ingredient_2 = classInstantiator.newInstance(ingredientsClass, "Toe of Frog", 3);

        final Object regicidePie = classInstantiator.newInstance(
                recipeAddedClass,
                asList(ingredient_1, ingredient_2),
                "Regicide Pie"
        );

        final String json = objectMapper.writeValueAsString(regicidePie);

        with(json)
                .assertThat("$.name", is("Regicide Pie"))
                .assertThat("$.ingredients[0].name", is("Eye of Newt"))
                .assertThat("$.ingredients[0].quantity", is(1))
                .assertThat("$.ingredients[1].name", is("Toe of Frog"))
                .assertThat("$.ingredients[1].quantity", is(3))
        ;

        generatorUtil.validate(jsonSchemaFile, json);
    }
}
