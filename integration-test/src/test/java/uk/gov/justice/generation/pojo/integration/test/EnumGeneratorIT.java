package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class EnumGeneratorIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/enum-generator");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGenerateJavaClassSourceCode() throws Exception {

        final String packageName = "uk.gov.justice.pojo.enumgenerator";
        final GenerationContext generationContext = new GenerationContext(sourceOutputDirectory.toPath(), packageName);

        final ClassDefinition studentDefinition = studentDefinition();
        final EnumDefinition colourDefinition = colourDefinition();

        final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory(new ClassNameFactory(packageName));

        final List<? extends Class<?>> classes = javaGeneratorFactory
                .createClassGeneratorsFor(asList(colourDefinition, studentDefinition), new DefaultPluginProvider())
                .stream()
                .map(classGenerator -> {
                    sourceWriter.write(classGenerator, generationContext);
                    return classCompiler.compile(classGenerator, generationContext, classesOutputDirectory);
                }).collect(toList());

        final String enumName = colourDefinition.getEnumValues().get(0);


        final Class<? extends Enum> enumClass = (Class<? extends Enum>) classes.get(0);
        final Enum red = Enum.valueOf(enumClass, enumName.toUpperCase());

        final Constructor<?> studentConstructor = classes.get(1).getConstructor(Integer.class, classes.get(0), String.class);
        final String name = "Fred";
        final Integer age = 21;
        final Object student = studentConstructor.newInstance(age, red, name);

        final String studentJson = objectMapper.writeValueAsString(student);

        with(studentJson)
                .assertThat("$.name", is(name))
                .assertThat("$.age", is(age))
                .assertThat("$.favouriteColour", is("Red"))
        ;
    }

    private ClassDefinition studentDefinition() {

        return new ClassDefinition(CLASS, "student")
                .addFieldDefinition(new StringDefinition("name", null))
                .addFieldDefinition(new FieldDefinition(INTEGER, "age"))
                .addFieldDefinition(colourDefinition());
    }

    private EnumDefinition colourDefinition() {

        return new EnumDefinition("favouriteColour", asList("Red", "Green", "Blue"));
    }
}
