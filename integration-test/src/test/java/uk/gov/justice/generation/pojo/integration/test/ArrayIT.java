package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.integration.utils.ClassInstantiator;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.generation.pojo.integration.utils.OutputDirectories;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class ArrayIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();
    private final ClassInstantiator classInstantiator = new ClassInstantiator();
    private final OutputDirectories outputDirectories = new OutputDirectories();

    @Before
    public void setup() throws Exception {
        outputDirectories.makeDirectories("./target/test-generation/arrays");
    }

    @Test
    public void shouldAnArraySchemaDocumentWithAnArrayOfItemSchemas() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/example.recipe-added.json");
        final String packageName = "uk.gov.justice.pojo.arrays";

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                outputDirectories);

        assertThat(newClasses.size(), is(2));

        final Class<?> ingredientsClass = newClasses.get(0);
        final Class<?> recipeAddedClass = newClasses.get(1);

        assertThat(ingredientsClass.getSimpleName(), is("Ingredients"));
        assertThat(recipeAddedClass.getSimpleName(), is("RecipeAdded"));

        final Object ingredient_1 = classInstantiator.newInstance(ingredientsClass, "Eye of Newt", 1);
        final Object ingredient_2 = classInstantiator.newInstance(ingredientsClass, "Toe of Frog", 3);

        final Object regicidePie = classInstantiator.newInstance(
                recipeAddedClass,
                false,
                asList(ingredient_1, ingredient_2),
                "Regicide Pie",
                "13"
        );

        final String json = objectMapper.writeValueAsString(regicidePie);

        with(json)
                .assertThat("$.name", is("Regicide Pie"))
                .assertThat("$.recipeId", is("13"))
                .assertThat("$.glutenFree", is(false))
                .assertThat("$.ingredients[0].name", is("Eye of Newt"))
                .assertThat("$.ingredients[0].quantity", is(1))
                .assertThat("$.ingredients[1].name", is("Toe of Frog"))
                .assertThat("$.ingredients[1].quantity", is(3))
        ;

        generatorUtil.validate(jsonSchemaFile, json);
    }
}
