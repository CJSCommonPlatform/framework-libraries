package uk.gov.justice.generation.pojo.write;


import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
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

        final TypeSpec helloWorld = simpleClassTypeSpec();

        final ClassGeneratable classGenerator = mock(ClassGeneratable.class);
        final GenerationContext generationContext = mock(GenerationContext.class);

        when(classGenerator.generate()).thenReturn(helloWorld);
        when(generationContext.getOutputDirectoryPath()).thenReturn(sourceOutputDirectory.toPath());
        when(generationContext.getPackageName()).thenReturn(packageName);

        for (final ClassGeneratable classGeneratable : singletonList(classGenerator)) {
            sourceWriter.write(classGeneratable, generationContext);
        }

        assertThat(sourceOutputDirectory.toPath().resolve("org/bloggs/fred/Address.java").toFile().exists(), is(true));
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldThrowExceptionIfUnableToWriteJavaFile() throws Exception {
        final String packageName = "org.bloggs.fred";

        final TypeSpec helloWorld = simpleClassTypeSpec();

        final ClassGeneratable classGenerator = mock(ClassGeneratable.class);
        final GenerationContext generationContext = mock(GenerationContext.class);

        when(classGenerator.generate()).thenReturn(helloWorld);
        when(generationContext.getOutputDirectoryPath()).thenReturn(sourceOutputDirectory.toPath());
        when(generationContext.getPackageName()).thenReturn(packageName);

        sourceOutputDirectory.setWritable(false);

        try {
            sourceWriter.write(classGenerator, generationContext);

            fail();
        } catch (final SourceCodeWriteException ex) {
            sourceOutputDirectory.setWritable(true);
            assertThat(ex.getMessage(), is("Failed to write java file to './target/test-generation' for 'org.bloggs.fred.Address.java'"));
            assertThat(ex.getCause(), is(instanceOf(IOException.class)));
        }
    }

    private TypeSpec simpleClassTypeSpec() {
        final MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        return TypeSpec.classBuilder("Address")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();
    }
}
