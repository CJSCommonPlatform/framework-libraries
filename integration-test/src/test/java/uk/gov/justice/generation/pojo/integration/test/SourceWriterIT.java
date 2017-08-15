package uk.gov.justice.generation.pojo.integration.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.bootstrap.GenerationContextFactory;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;
import uk.gov.justice.generation.pojo.integration.utils.ClassCompiler;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class SourceWriterIT {

    private final SourceWriter sourceWriter = new SourceWriter();
    private final ClassCompiler classCompiler = new ClassCompiler();

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    @Test
    public void shouldWriteASingleSourceFile() throws Exception {

        final File rootOutputDirectory = new File("target/sources-generated-from-tests");
        rootOutputDirectory.delete();
        final String packageName = "org.bloggs.fred";

        final ClassDefinition addressDefinition = addressDefinition(packageName);


        final File sourceOutputDirectory = new File("./target/test-generation");
        final File classesOutputDirectory = new File("./target/test-classes");

        final GenerationContext generationContext = new GenerationContextFactory().create(sourceOutputDirectory);

        sourceOutputDirectory.delete();

        final ClassGeneratable addressGenerator = new JavaGeneratorFactory().createClassGeneratorFor(addressDefinition);
        sourceWriter.write(addressGenerator, generationContext);
        final Class<?> addressClass = classCompiler.compile(addressGenerator, sourceOutputDirectory, classesOutputDirectory);

        assertThat(addressClass.getName(), is(addressDefinition.getClassName().getFullyQualifiedName()));

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
        final ClassDefinition addressDefinition = new ClassDefinition("address", new ClassName(packageName, "Address"));
        addressDefinition.addFieldDefinition(new FieldDefinition("firstLine", new ClassName(String.class)));
        addressDefinition.addFieldDefinition(new FieldDefinition("postCode", new ClassName(String.class)));

        return addressDefinition;
    }
}
