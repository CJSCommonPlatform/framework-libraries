package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class SourceWriterIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

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
        final ClassDefinition addressDefinition = addressDefinition(packageName);
        final GenerationContext generationContext = new GenerationContext(sourceOutputDirectory.toPath(), packageName);

        final ClassGeneratable addressGenerator = new JavaGeneratorFactory(new ClassNameFactory(packageName))
                .createClassGeneratorsFor(singletonList(addressDefinition), new DefaultPluginProvider())
                .get(0);

        sourceWriter.write(addressGenerator, generationContext);
        final Class<?> addressClass = classCompiler.compile(addressGenerator, generationContext, classesOutputDirectory);

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

    private ClassDefinition addressDefinition(final String packageName) {
        final ClassDefinition addressDefinition = new ClassDefinition(CLASS, "address");
        addressDefinition.addFieldDefinition(new StringDefinition("firstLine", null));
        addressDefinition.addFieldDefinition(new StringDefinition("postCode", null));

        return addressDefinition;
    }
}
