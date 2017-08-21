package uk.gov.justice.generation.pojo.write;

import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class SourceWriterTest {

    private final SourceWriter sourceWriter = new SourceWriter();

    private File sourceOutputDirectory;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File("./target/test-generation");

        sourceOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    @Test
    public void shouldWriteASingleSourceFile() throws Exception {
        final String packageName = "org.bloggs.fred";
        final ClassDefinition addressDefinition = addressDefinition(packageName);

        new JavaGeneratorFactory()
                .createClassGeneratorsFor(singletonList(addressDefinition))
                .forEach(classGeneratable -> sourceWriter.write(classGeneratable, sourceOutputDirectory.toPath()));

        assertThat(sourceOutputDirectory.toPath().resolve("org/bloggs/fred/Address.java").toFile().exists(), is(true));
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldThrowExceptionIfUnableToWriteJavaFile() throws Exception {
        final String packageName = "org.bloggs.fred";
        final ClassDefinition addressDefinition = addressDefinition(packageName);

        sourceOutputDirectory.setWritable(false);

        try {
            new JavaGeneratorFactory()
                    .createClassGeneratorsFor(singletonList(addressDefinition))
                    .forEach(classGeneratable -> sourceWriter.write(classGeneratable, sourceOutputDirectory.toPath()));

            fail();
        } catch (SourceCodeWriteException ex) {
            sourceOutputDirectory.setWritable(true);
            assertThat(ex.getMessage(), is("Failed to write java file to './target/test-generation' for 'org.bloggs.fred.Address.java'"));
            assertThat(ex.getCause(), is(instanceOf(IOException.class)));
        }
    }

    private ClassDefinition addressDefinition(final String packageName) {
        final ClassDefinition addressDefinition = new ClassDefinition("address", new ClassName(packageName, "Address"));
        addressDefinition.addFieldDefinition(new FieldDefinition("firstLine", new ClassName(String.class)));
        addressDefinition.addFieldDefinition(new FieldDefinition("postCode", new ClassName(String.class)));

        return addressDefinition;
    }
}