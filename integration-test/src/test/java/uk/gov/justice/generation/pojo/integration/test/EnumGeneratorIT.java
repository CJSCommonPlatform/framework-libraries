package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
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
    private final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactory();

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

        final ClassDefinition studentDefinition = studentDefinition(packageName);
        final EnumDefinition colourDefinition = colourDefinition(packageName);

        final List<? extends Class<?>> classes = javaGeneratorFactory
                .createClassGeneratorsFor(asList(colourDefinition, studentDefinition))
                .stream()
                .map(classGenerator -> {
                    sourceWriter.write(classGenerator, sourceOutputDirectory.toPath());
                    return classCompiler.compile(classGenerator, sourceOutputDirectory, classesOutputDirectory);
                }).collect(toList());

        assertThat(classes.get(0).getName(), is(colourDefinition.getClassName().getFullyQualifiedName()));
        assertThat(classes.get(1).getName(), is(studentDefinition.getClassName().getFullyQualifiedName()));

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

    private ClassDefinition studentDefinition(final String packageName) {

        return new ClassDefinition("student", new ClassName(packageName, "Student"))
                .addFieldDefinition(new FieldDefinition("name", new ClassName(String.class)))
                .addFieldDefinition(new FieldDefinition("age", new ClassName(Integer.class)))
                .addFieldDefinition(new FieldDefinition("favouriteColour", new ClassName(packageName, "Colour")))
                ;
    }

    private EnumDefinition colourDefinition(final String packageName) {

        return new EnumDefinition("favouriteColour", new ClassName(packageName, "Colour"), asList("Red", "Green", "Blue"));
    }
}
