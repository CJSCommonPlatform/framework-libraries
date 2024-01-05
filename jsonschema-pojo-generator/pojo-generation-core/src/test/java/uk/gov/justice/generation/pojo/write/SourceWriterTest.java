package uk.gov.justice.generation.pojo.write;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.IOException;
import java.nio.file.Path;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SourceWriterTest {

    private final static String TEST_SOURCE_OUTPUT_DIR_NAME = "./target/test-generation";
    private final static String TEST_PACKAGE_NAME = "org.bloggs.fred";

    @Mock
    private JavaPoetJavaFileCreator javaPoetJavaFileCreator;

    @InjectMocks
    private SourceWriter sourceWriter;

    @Test
    public void shouldWriteASingleSourceFile() throws Exception {
        final TypeSpec helloWorld = simpleClassTypeSpec();
        final Path path = get(TEST_SOURCE_OUTPUT_DIR_NAME);

        final ClassGeneratable classGenerator = mock(ClassGeneratable.class);
        final GenerationContext generationContext = mock(GenerationContext.class);
        final JavaFile javaFile = mock(JavaFile.class);

        when(classGenerator.generate()).thenReturn(helloWorld);
        when(classGenerator.getPackageName()).thenReturn(TEST_PACKAGE_NAME);
        when(generationContext.getOutputDirectoryPath()).thenReturn(path);
        when(javaPoetJavaFileCreator.createJavaFile(helloWorld, TEST_PACKAGE_NAME)).thenReturn(javaFile);

        sourceWriter.write(classGenerator, generationContext);

        verify(javaFile).writeTo(path);
    }

    @Test
    @SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
    public void shouldThrowExceptionIfUnableToWriteJavaFile() throws Exception {

        final TypeSpec helloWorld = simpleClassTypeSpec();
        final Path path = get(TEST_SOURCE_OUTPUT_DIR_NAME);
        final IOException ioException = new IOException();

        final ClassGeneratable classGenerator = mock(ClassGeneratable.class);
        final GenerationContext generationContext = mock(GenerationContext.class);
        final JavaFile javaFile = mock(JavaFile.class);

        when(classGenerator.generate()).thenReturn(helloWorld);
        when(classGenerator.getPackageName()).thenReturn(TEST_PACKAGE_NAME);
        when(generationContext.getOutputDirectoryPath()).thenReturn(path);
        when(javaPoetJavaFileCreator.createJavaFile(helloWorld, TEST_PACKAGE_NAME)).thenReturn(javaFile);

        doThrow(ioException).when(javaFile).writeTo(path);

        final SourceCodeWriteException sourceCodeWriteException = assertThrows(
                SourceCodeWriteException.class,
                () -> sourceWriter.write(classGenerator, generationContext));

        assertThat(sourceCodeWriteException.getCause(), is(ioException));
        assertThat(sourceCodeWriteException.getMessage(), is("Failed to write java file to './target/test-generation' for 'org.bloggs.fred.Address.java'"));
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
