package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.integration.utils.GeneratorUtil;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class SourceWriterIT {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final GeneratorUtil generatorUtil = new GeneratorUtil();

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation/source-writer");
        classesOutputDirectory = new File("./target/test-classes");

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldWriteASingleSourceFile() throws Exception {

        final String packageName = "org.bloggs.fred";
        final String sourceFilename = "filename.json";
        final ClassDefinition addressDefinition = addressDefinition();

        final List<Class<?>> classes = generatorUtil.generateAndCompileFromDefinitions(
                sourceFilename,
                packageName,
                sourceOutputDirectory.toPath(),
                classesOutputDirectory.toPath(),
                singletonList(addressDefinition));

        final Class<?> addressClass = classes.get(0);

        final String firstLine = "firstLine";
        final String postCode = "postCode";

        final Constructor<?> addressConstructor = addressClass.getConstructor(String.class, String.class);
        final Object address = addressConstructor.newInstance(firstLine, postCode);

        final String addressJson = objectMapper.writeValueAsString(address);

        with(addressJson)
                .assertThat("$.firstLine", is(firstLine))
                .assertThat("$.postCode", is(postCode))
        ;
    }

    private ClassDefinition addressDefinition() {
        final ClassDefinition addressDefinition = new ClassDefinition(CLASS, "address");
        addressDefinition.addFieldDefinition(new FieldDefinition(STRING, "firstLine"));
        addressDefinition.addFieldDefinition(new FieldDefinition(STRING, "postCode"));

        return addressDefinition;
    }
}
