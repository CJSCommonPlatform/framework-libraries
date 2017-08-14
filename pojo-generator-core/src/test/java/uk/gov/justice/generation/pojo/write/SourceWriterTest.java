package uk.gov.justice.generation.pojo.write;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.bootstrap.GenerationContextFactory;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;

public class SourceWriterTest {

    private final SourceWriter sourceWriter = new SourceWriter();

    @Test
    public void shouldWriteASingleSourceFile() throws Exception {

        final File rootOutputDirectory = new File("target/sources-generated-from-tests");
        rootOutputDirectory.delete();
        final String packageName = "org.bloggs.fred";

        final ClassDefinition addressDefinition = addressDefinition(packageName);

        final File sourceOutputDirectory = new File("./target/test-generation");

        sourceOutputDirectory.mkdirs();

        final GenerationContext generationContext = new GenerationContextFactory().create(sourceOutputDirectory);

        sourceOutputDirectory.delete();

        final ClassGeneratable addressGenerator = new JavaGeneratorFactory().createClassGeneratorFor(addressDefinition);
        sourceWriter.write(addressGenerator, generationContext);

        assertThat(Paths.get(sourceOutputDirectory.toPath().toString(), "org/bloggs/fred/Address.java").toFile().exists(), is(true));
    }

    private ClassDefinition addressDefinition(final String packageName) {
        final ClassDefinition addressDefinition = new ClassDefinition("address", new ClassName(packageName, "Address"));
        addressDefinition.addFieldDefinition(new FieldDefinition("firstLine", new ClassName(String.class)));
        addressDefinition.addFieldDefinition(new FieldDefinition("postCode", new ClassName(String.class)));

        return addressDefinition;
    }
}