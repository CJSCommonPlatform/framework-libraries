package uk.gov.justice.generation.io.files;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JavaFileSimpleNameListerTest {

    @InjectMocks
    private JavaFileSimpleNameLister javaFileSimpleNameLister;

    @Test
    public void shouldAppendThePackageNameToEachSourceDirectoryAndListTheSimpleNameOfEachJavaFile() throws Exception {

        final String packageName = "org.bloggs.fred";
        final String packageNameAsPath = "org/bloggs/fred";

        final Path sourcePath_1 = mock(Path.class);
        final Path sourcePath_2 = mock(Path.class);
        final Path outputDirectory = mock(Path.class);
        final Path resolvedSourcePath_1 = mock(Path.class, RETURNS_DEEP_STUBS);
        final Path resolvedSourcePath_2 = mock(Path.class, RETURNS_DEEP_STUBS);
        final File javaFile_1 = mock(File.class);
        final File javaFile_2 = mock(File.class);
        final File javaFile_3 = mock(File.class);
        final File javaFile_4 = mock(File.class);

        when(sourcePath_1.resolve(packageNameAsPath)).thenReturn(resolvedSourcePath_1);
        when(sourcePath_2.resolve(packageNameAsPath)).thenReturn(resolvedSourcePath_2);

        when(resolvedSourcePath_1.toFile().listFiles()).thenReturn(new File[]{javaFile_1, javaFile_2});
        when(resolvedSourcePath_2.toFile().listFiles()).thenReturn(new File[]{javaFile_3, javaFile_4});

        when(javaFile_1.getName()).thenReturn("FirstClass.java");
        when(javaFile_2.getName()).thenReturn("SecondClass.java");
        when(javaFile_3.getName()).thenReturn("ThirdClass.java");
        when(javaFile_4.getName()).thenReturn("FourthClass.java");

        final List<String> simpleNames = javaFileSimpleNameLister.findSimpleNames(
                asList(sourcePath_1, sourcePath_2),
                outputDirectory,
                packageName);

        assertThat(simpleNames, hasItems("FirstClass", "SecondClass", "ThirdClass", "FourthClass"));
    }

    @Test
    public void shouldHandleANullListingOfFilesInDirectory() throws Exception {

        final String packageName = "org.bloggs.fred";
        final String packageNameAsPath = "org/bloggs/fred";

        final Path sourcePath_1 = mock(Path.class);
        final Path sourcePath_2 = mock(Path.class);
        final Path outputDirectory = mock(Path.class);
        final Path resolvedSourcePath_1 = mock(Path.class, RETURNS_DEEP_STUBS);
        final Path resolvedSourcePath_2 = mock(Path.class, RETURNS_DEEP_STUBS);
        final File javaFile_1 = mock(File.class);
        final File javaFile_2 = mock(File.class);

        when(sourcePath_1.resolve(packageNameAsPath)).thenReturn(resolvedSourcePath_1);
        when(sourcePath_2.resolve(packageNameAsPath)).thenReturn(resolvedSourcePath_2);

        when(resolvedSourcePath_1.toFile().listFiles()).thenReturn(new File[]{javaFile_1, javaFile_2});
        when(resolvedSourcePath_2.toFile().listFiles()).thenReturn(null);

        when(javaFile_1.getName()).thenReturn("FirstClass.java");
        when(javaFile_2.getName()).thenReturn("SecondClass.java");

        final List<String> simpleNames = javaFileSimpleNameLister.findSimpleNames(
                asList(sourcePath_1, sourcePath_2),
                outputDirectory,
                packageName);

        assertThat(simpleNames, hasItems("FirstClass", "SecondClass"));
    }

    @Test
    public void shouldIgnoreFilesWithoutTheJavaExtension() throws Exception {

        final String packageName = "org.bloggs.fred";
        final String packageNameAsPath = "org/bloggs/fred";

        final Path sourcePath_1 = mock(Path.class);
        final Path sourcePath_2 = mock(Path.class);
        final Path outputDirectory = mock(Path.class);
        final Path resolvedSourcePath_1 = mock(Path.class, RETURNS_DEEP_STUBS);
        final Path resolvedSourcePath_2 = mock(Path.class, RETURNS_DEEP_STUBS);
        final File javaFile_1 = mock(File.class);
        final File javaFile_2 = mock(File.class);
        final File javaFile_3 = mock(File.class);
        final File javaFile_4 = mock(File.class);

        when(sourcePath_1.resolve(packageNameAsPath)).thenReturn(resolvedSourcePath_1);
        when(sourcePath_2.resolve(packageNameAsPath)).thenReturn(resolvedSourcePath_2);

        when(resolvedSourcePath_1.toFile().listFiles()).thenReturn(new File[]{javaFile_1, javaFile_2});
        when(resolvedSourcePath_2.toFile().listFiles()).thenReturn(new File[]{javaFile_3, javaFile_4});

        when(javaFile_1.getName()).thenReturn("FirstClass.java");
        when(javaFile_2.getName()).thenReturn("SecondClass.txt");
        when(javaFile_3.getName()).thenReturn("ThirdClass.java");
        when(javaFile_4.getName()).thenReturn("FourthClass.jpg");

        final List<String> simpleNames = javaFileSimpleNameLister.findSimpleNames(
                asList(sourcePath_1, sourcePath_2),
                outputDirectory,
                packageName);

        assertThat(simpleNames, hasItems("FirstClass", "ThirdClass"));
    }

    @Test
    public void shouldIgnoreFilesInOutputDirectory() throws Exception {

        final String packageName = "org.bloggs.fred";
        final String packageNameAsPath = "org/bloggs/fred";

        final Path sourcePath_1 = mock(Path.class);
        final Path outputDirectory = mock(Path.class);
        final Path resolvedSourcePath_1 = mock(Path.class, RETURNS_DEEP_STUBS);
        final File javaFile_1 = mock(File.class);
        final File javaFile_2 = mock(File.class);

        when(sourcePath_1.resolve(packageNameAsPath)).thenReturn(resolvedSourcePath_1);

        when(resolvedSourcePath_1.toFile().listFiles()).thenReturn(new File[]{javaFile_1, javaFile_2});

        when(javaFile_1.getName()).thenReturn("FirstClass.java");
        when(javaFile_2.getName()).thenReturn("SecondClass.java");

        final List<String> simpleNames = javaFileSimpleNameLister.findSimpleNames(
                asList(sourcePath_1, outputDirectory),
                outputDirectory,
                packageName);

        assertThat(simpleNames, hasItems("FirstClass", "SecondClass"));

        verifyZeroInteractions(outputDirectory);
    }
}
