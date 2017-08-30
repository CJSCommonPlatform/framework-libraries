package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.ClassNameProvider;
import uk.gov.justice.generation.pojo.core.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.core.DefinitionFactory;
import uk.gov.justice.generation.pojo.core.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ArrayIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final NameGenerator rootFieldNameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final DefinitionFactory definitionFactory = new DefinitionFactory(new ClassNameProvider());

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
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.rootFieldNameFrom(jsonSchemaFile);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo.arrays", definitionFactory);
        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(schema);

        jsonSchemaWrapper.accept(fieldName, definitionBuilderVisitor);

        final List<Class<?>> newClasses = new ArrayList<>();

        javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor.getDefinitions())
                .forEach(classGeneratable -> {
                    sourceWriter.write(classGeneratable, sourceOutputDirectory.toPath());
                    final Class<?> newClass = classCompiler.compile(classGeneratable, sourceOutputDirectory, classesOutputDirectory);
                    newClasses.add(newClass);
                });

        assertThat(newClasses.size(), is(2));
        assertThat(newClasses.get(0).getSimpleName(), is("Ingredients"));
        assertThat(newClasses.get(1).getSimpleName(), is("RecipeAdded"));

        final Constructor<?> ingredientsConstructor = newClasses.get(0).getConstructor(
                Integer.class,
                String.class);
        final Constructor<?> recipeAddedConstructor = newClasses.get(1).getConstructor(
                String.class,
                Boolean.class,
                List.class,
                String.class);

        final Object ingredient_1 = ingredientsConstructor.newInstance(1, "Eye of Newt");
        final Object ingredient_2 = ingredientsConstructor.newInstance(3, "Toe of Frog");

        final Object regicidePie = recipeAddedConstructor.newInstance(
                "Regicide Pie",
                false,
                asList(ingredient_1, ingredient_2),
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
