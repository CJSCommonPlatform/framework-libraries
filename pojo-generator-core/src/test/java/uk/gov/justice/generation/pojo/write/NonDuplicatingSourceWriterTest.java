package uk.gov.justice.generation.pojo.write;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGenerator;

import java.io.File;
import java.nio.file.Path;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;


@RunWith(MockitoJUnitRunner.class)
public class NonDuplicatingSourceWriterTest {

    @Mock
    private JavaSourceFileProvider javaSourceFileProvider;

    @Mock
    private SourceWriter sourceWriter;

    @InjectMocks
    private NonDuplicatingSourceWriter nonDuplicatingSourceWriter;

    @Test
    public void shouldWriteANewJavaFile() throws Exception {

        final String fileName = "MyFunkyNewPojo.java";

        final GenerationContext generationContext = mock(GenerationContext.class);
        final ClassGenerator classGenerator = mock(ClassGenerator.class);
        final Path sourceRootDirectory = mock(Path.class);
        final File sourceFile = mock(File.class);
        final Logger logger = mock(Logger.class);

        when(generationContext.getOutputDirectoryPath()).thenReturn(sourceRootDirectory);
        when(javaSourceFileProvider.getJavaFile(generationContext, classGenerator)).thenReturn(sourceFile);
        when(sourceFile.exists()).thenReturn(false).thenReturn(true);
        when(generationContext.getLoggerFor(NonDuplicatingSourceWriter.class)).thenReturn(logger);
        when(sourceFile.getName()).thenReturn(fileName);

        assertThat(nonDuplicatingSourceWriter.write(classGenerator, generationContext), is(sourceFile));

        final InOrder inOrder = inOrder(sourceWriter, logger);

        inOrder.verify(sourceWriter).write(classGenerator, generationContext);
        inOrder.verify(logger).info("Wrote new Java file '{}'", "MyFunkyNewPojo.java");
    }

    @Test
    public void shouldNotWriteANewJavaFileIfTheFileAlreadyExists() throws Exception {

        final GenerationContext generationContext = mock(GenerationContext.class);
        final ClassGenerator classGenerator = mock(ClassGenerator.class);
        final Path sourceRootDirectory = mock(Path.class);
        final File sourceFile = mock(File.class);
        final Logger logger = mock(Logger.class);

        when(generationContext.getLoggerFor(NonDuplicatingSourceWriter.class)).thenReturn(logger);
        when(generationContext.getOutputDirectoryPath()).thenReturn(sourceRootDirectory);
        when(javaSourceFileProvider.getJavaFile(generationContext, classGenerator)).thenReturn(sourceFile);
        when(sourceFile.exists()).thenReturn(true);
        when(sourceFile.getAbsolutePath()).thenReturn("org/test/MyExistingPojo.java");

        assertThat(nonDuplicatingSourceWriter.write(classGenerator, generationContext), is(sourceFile));

        verifyZeroInteractions(sourceWriter);
        verify(logger).info("Skipping generation, Java file already exists: '{}'", "org/test/MyExistingPojo.java");
    }

    @Test
    public void shouldFailIfTheNewJavaFileDoesNotExistsAfterWriting() throws Exception {

        final String absolutePath = "path/to/MyFunkyNewPojo.java";

        final GenerationContext generationContext = mock(GenerationContext.class);
        final ClassGenerator classGenerator = mock(ClassGenerator.class);
        final Path sourceRootDirectory = mock(Path.class);
        final File sourceFile = mock(File.class);
        final Logger logger = mock(Logger.class);

        when(generationContext.getOutputDirectoryPath()).thenReturn(sourceRootDirectory);
        when(javaSourceFileProvider.getJavaFile(generationContext, classGenerator)).thenReturn(sourceFile);
        when(sourceFile.exists()).thenReturn(false).thenReturn(false);
        when(generationContext.getLoggerFor(NonDuplicatingSourceWriter.class)).thenReturn(logger);
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);

        try {
            nonDuplicatingSourceWriter.write(classGenerator, generationContext);
            fail();
        } catch (final SourceCodeWriteException expected) {
            assertThat(expected.getMessage(), is("Failed to write java file '" + absolutePath + "'"));
        }

    }
}
