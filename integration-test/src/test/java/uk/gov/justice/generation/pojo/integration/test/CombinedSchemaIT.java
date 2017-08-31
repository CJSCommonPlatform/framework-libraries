package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.core.ClassNameProvider;
import uk.gov.justice.generation.pojo.core.NameGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.visitable.VisitableSchema;
import uk.gov.justice.generation.pojo.visitable.VisitableSchemaFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorFactory;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
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

public class CombinedSchemaIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();
    private final NameGenerator rootFieldNameGenerator = new NameGenerator();
    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final DefaultDefinitionFactory definitionFactory = new DefaultDefinitionFactory(new ClassNameProvider());

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

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor("uk.gov.justice.pojo.combined.schema", definitionFactory);
        final VisitableSchemaFactory visitableSchemaFactory = new VisitableSchemaFactory();
        final VisitableSchema visitableSchema = visitableSchemaFactory.createWith(schema, new DefaultAcceptorFactory(visitableSchemaFactory));

        visitableSchema.accept(fieldName, definitionBuilderVisitor);

        final List<Class<?>> newClasses = new ArrayList<>();

        javaGeneratorFactory
                .createClassGeneratorsFor(definitionBuilderVisitor.getDefinitions(), new DefaultPluginProvider())
                .forEach(classGeneratable -> {
                    sourceWriter.write(classGeneratable, sourceOutputDirectory.toPath());
                    final Class<?> newClass = classCompiler.compile(classGeneratable, sourceOutputDirectory, classesOutputDirectory);
                    newClasses.add(newClass);
                });

        assertThat(newClasses.size(), is(4));
        assertThat(newClasses.get(0).getSimpleName(), is("UkComms"));
        assertThat(newClasses.get(1).getSimpleName(), is("UsComms"));
        assertThat(newClasses.get(2).getSimpleName(), is("Communication"));
        assertThat(newClasses.get(3).getSimpleName(), is("Address"));

        final Object ukComms = newClasses.get(0).getConstructor(Boolean.class).newInstance(true);
        final Object usComms = newClasses.get(1).getConstructor(Boolean.class).newInstance(true);
        final Object communication = newClasses.get(2).getConstructor(Boolean.class).newInstance(true);

        final Constructor<?> addressConstructor = newClasses.get(3).getConstructor(
                String.class,
                String.class,
                String.class,
                communication.getClass(),
                String.class,
                usComms.getClass(),
                String.class,
                String.class,
                String.class,
                ukComms.getClass());

        assertThat(addressConstructor, is(notNullValue()));

        final String city = "city";
        final String county = "county";
        final String addressLine1 = "addressLine1";
        final String addressLine2 = "addressLine2";
        final String postCode = "postCode";

        final String nullZipCode = null;
        final String nullState = null;

        final Object addressObject = addressConstructor.newInstance(
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

        schema.validate(new JSONObject(json));
    }
}
