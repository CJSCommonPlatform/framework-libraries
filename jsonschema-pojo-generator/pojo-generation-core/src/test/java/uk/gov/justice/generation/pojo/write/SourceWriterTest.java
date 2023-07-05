package uk.gov.justice.generation.pojo.write;

import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SourceWriterTest {

    private final static String TEST_SOURCE_OUTPUT_DIR_NAME = "./target/test-generation";
    private final static String TEST_PACKAGE_NAME = "org.bloggs.fred";

    private final SourceWriter sourceWriter = new SourceWriter();

    private File sourceOutputDirectory;

    @BeforeEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() throws Exception {
        sourceOutputDirectory = new File(TEST_SOURCE_OUTPUT_DIR_NAME);

        sourceOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }

    }

    @Test
    public void shouldWriteASingleSourceFile() throws Exception {
        final TypeSpec helloWorld = simpleClassTypeSpec();

        final ClassGeneratable classGenerator = mock(ClassGeneratable.class);
        final GenerationContext generationContext = mock(GenerationContext.class);

        when(classGenerator.generate()).thenReturn(helloWorld);
        when(classGenerator.getPackageName()).thenReturn(TEST_PACKAGE_NAME);
        when(generationContext.getOutputDirectoryPath()).thenReturn(sourceOutputDirectory.toPath());

        for (final ClassGeneratable classGeneratable : singletonList(classGenerator)) {
            sourceWriter.write(classGeneratable, generationContext);
        }

        assertThat(sourceOutputDirectory.toPath().resolve("org/bloggs/fred/Address.java").toFile().exists(), is(true));
    }

    @Test
    @SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
    public void shouldThrowExceptionIfUnableToWriteJavaFile() throws Exception {

        final TypeSpec helloWorld = simpleClassTypeSpec();

        final ClassGeneratable classGenerator = mock(ClassGeneratable.class);
        final GenerationContext generationContext = mock(GenerationContext.class);

        final Path destPathMock = mock(Path.class);
        final FileSystem fileSystemMock = mock(FileSystem.class);
        final FileSystemProvider providerMock = mock(FileSystemProvider.class);

        // Next few lines provide mocking to enable IOException to be thrown from
        // FileSystemProvider.newOutputstream()
        when(destPathMock.getFileSystem()).thenReturn(fileSystemMock);
        when(destPathMock.toString()).thenReturn(TEST_SOURCE_OUTPUT_DIR_NAME);
        when(destPathMock.resolve(anyString())).thenReturn(destPathMock);

        when(fileSystemMock.provider()).thenReturn(providerMock);

        final BasicFileAttributes basicFileAttributes = Files.readAttributes(sourceOutputDirectory.toPath(), BasicFileAttributes.class);
        when(providerMock.readAttributes(any(Path.class), any(Class.class))).thenReturn(basicFileAttributes);
        when(providerMock.newOutputStream(any(Path.class), any())).thenThrow(new IOException());

        when(classGenerator.generate()).thenReturn(helloWorld);
        when(classGenerator.getPackageName()).thenReturn(TEST_PACKAGE_NAME);
        when(generationContext.getOutputDirectoryPath()).thenReturn(destPathMock);

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
