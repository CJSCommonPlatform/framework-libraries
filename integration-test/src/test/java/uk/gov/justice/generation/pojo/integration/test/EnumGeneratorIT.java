package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class EnumGeneratorIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

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
        final String sourceFilename = "filenam.json";

        final ClassDefinition studentDefinition = studentDefinition();
        final EnumDefinition colourDefinition = colourDefinition();
        final List<Definition> definitions = asList(colourDefinition, studentDefinition);

        final List<Class<?>> classes = generatorUtil.generateAndCompileFromDefinitions(
                sourceFilename,
                packageName,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath(),
                definitions);

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
                .addFieldDefinition(new FieldDefinition(STRING, "name"))
                .addFieldDefinition(new FieldDefinition(INTEGER, "age"))
                .addFieldDefinition(colourDefinition());
    }

    private EnumDefinition colourDefinition() {

        return new EnumDefinition("favouriteColour", asList("Red", "Green", "Blue"));
    }
}
