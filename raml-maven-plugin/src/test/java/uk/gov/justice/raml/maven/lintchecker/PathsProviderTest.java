package uk.gov.justice.raml.maven.lintchecker;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.FileTreeScannerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PathsProviderTest {

    @Mock
    private FileTreeScannerFactory scannerFactory;

    @InjectMocks
    private PathsProvider pathsProvider;

    @Test
    public void shouldScanTheCorrectDirectoryForPaths() throws Exception {

        final FileTreeScanner fileTreeScanner = mock(FileTreeScanner.class);
        final File sourceDirectory = new File(".");

        final String[] includes = {"include_1"};
        final String[] excludes = {"exclude_1"};

        final Collection<Path> paths = singletonList(get("."));

        when(scannerFactory.create()).thenReturn(fileTreeScanner);
        when(fileTreeScanner.find(sourceDirectory.toPath(), includes, excludes)).thenReturn(paths);

        assertThat(pathsProvider.getPaths(sourceDirectory, asList(includes), asList(excludes)), is(paths));
    }

    @Test
    public void shouldThrowAMojoFailureExceptionIfFindingPathsFails() throws Exception {

        final IOException ioException = new IOException("Oops");

        final FileTreeScanner fileTreeScanner = mock(FileTreeScanner.class);
        final File sourceDirectory = new File("my-directory");

        final String[] includes = {"include_1"};
        final String[] excludes = {"exclude_1"};

        when(scannerFactory.create()).thenReturn(fileTreeScanner);
        when(fileTreeScanner.find(sourceDirectory.toPath(), includes, excludes)).thenThrow(ioException);

        try {
            pathsProvider.getPaths(sourceDirectory, asList(includes), asList(excludes));
            fail();
        } catch (final MojoFailureException expected) {
            assertThat(expected.getCause(), is(ioException));
            assertThat(expected.getMessage(), is("Failed to find paths for source directory my-directory"));
        }
    }
}
