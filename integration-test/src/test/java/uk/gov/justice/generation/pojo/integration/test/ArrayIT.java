package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ArrayIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/arrays");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldAnArraySchemaDocumentWithAnArrayOfItemSchemas() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/example.recipe-added.json");
        final String packageName = "uk.gov.justice.pojo.arrays";
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);

        final List<Class<?>> newClasses = generatorUtil.generateAndCompileJavaSource(
                jsonSchemaFile,
                packageName,
                schema,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath());

        assertThat(newClasses.size(), is(2));
        assertThat(newClasses.get(0).getSimpleName(), is("Ingredients"));
        assertThat(newClasses.get(1).getSimpleName(), is("RecipeAdded"));

        final Constructor<?> ingredientsConstructor = newClasses.get(0).getConstructor(
                String.class, Integer.class);
        final Constructor<?> recipeAddedConstructor = newClasses.get(1).getConstructor(
                Boolean.class,
                List.class,
                String.class,
                String.class);

        final Object ingredient_1 = ingredientsConstructor.newInstance("Eye of Newt", 1);
        final Object ingredient_2 = ingredientsConstructor.newInstance("Toe of Frog", 3);

        final Object regicidePie = recipeAddedConstructor.newInstance(
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

        schema.validate(new JSONObject(json));
    }
}
