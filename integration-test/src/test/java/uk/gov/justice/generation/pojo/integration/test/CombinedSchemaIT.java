package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.DefinitionBuilderVisitor;
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
import org.junit.Before;
import org.junit.Test;

public class CombinedSchemaIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final NameGenerator rootFieldNameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/combined-schema");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldParseAVeryComplexSchemaDocument() throws Exception {

        final File jsonSchemaFile = new File("src/test/resources/schemas/address.json");
        final Schema schema = schemaLoader.loadFrom(jsonSchemaFile);
        final String fieldName = rootFieldNameGenerator.rootFieldNameFrom(jsonSchemaFile);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo.combined.schema");
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

        assertThat(newClasses.size(), is(1));
        assertThat(newClasses.get(0).getSimpleName(), is("Address"));

        final Constructor<?> constructor = newClasses.get(0).getConstructor(
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                String.class);

        assertThat(constructor, is(notNullValue()));

        final String zipCode = "zipCode";
        final String city = "city";
        final String county = "county";
        final String addressLine1 = "addressLine1";
        final String addressLine2 = "addressLine2";
        final String postCode = "postCode";
        final String state = "state";

        final Object addressObject = constructor.newInstance(
                zipCode,
                city,
                county,
                addressLine1,
                addressLine2,
                postCode,
                state
        );

        final String json = objectMapper.writeValueAsString(addressObject);

        with(json)
                .assertThat("$.addressLine1", is("addressLine1"))
                .assertThat("$.addressLine2", is("addressLine2"))
                .assertThat("$.city", is("city"))
                .assertThat("$.county", is("county"))
                .assertThat("$.postCode", is("postCode"))
                .assertThat("$.state", is("state"))
                .assertThat("$.zipCode", is("zipCode"))
        ;
    }
}
