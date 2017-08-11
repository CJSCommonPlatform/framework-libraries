package uk.gov.justice.maven.generator.io.files.parser.io;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * Unit tests for the {@link FileTreeScanner} class.
 */
public class FileTreeScannerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindMatchingFilesInBaseDir() throws Exception {

        String[] includes = {"**/*.raml"};
        String[] excludes = {"**/*ignore.raml"};
        Path baseDir = Paths.get("src/test/resources/raml/");

        FileTreeScanner fileTreeResolver = new FileTreeScanner();

        Collection<Path> paths = fileTreeResolver.find(baseDir, includes, excludes);

        assertThat(paths, hasSize(3));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("example-2.raml")),
                equalTo(Paths.get("subfolder/example-3.raml"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindMatchingFilesInClasspathIfBaseDirSetToCLASSPATH() throws Exception {

        String[] includes = {"**/*.raml"};
        String[] excludes = {"**/*ignore.raml"};
        Path baseDir = Paths.get("CLASSPATH");

        FileTreeScanner fileTreeResolver = new FileTreeScanner();

        Collection<Path> paths = fileTreeResolver.find(baseDir, includes, excludes);

        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("raml/external-3.raml")),
                equalTo(Paths.get("raml/external-4.raml")),
                equalTo(Paths.get("raml/example-1.raml")),
                equalTo(Paths.get("raml/example-2.raml")),
                equalTo(Paths.get("raml/subfolder/example-3.raml"))
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindMatchingFilesInClasspathIfBaseDirNULL() throws Exception {

        String[] includes = {"**/*.raml"};
        String[] excludes = {"**/*ignore.raml"};

        FileTreeScanner fileTreeResolver = new FileTreeScanner();

        Collection<Path> paths = fileTreeResolver.find(null, includes, excludes);

        assertThat(paths, hasSize(5));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("raml/external-3.raml")),
                equalTo(Paths.get("raml/external-4.raml")),
                equalTo(Paths.get("raml/example-1.raml")),
                equalTo(Paths.get("raml/example-2.raml")),
                equalTo(Paths.get("raml/subfolder/example-3.raml"))));
    }


    @Test
    @SuppressWarnings("unchecked")
    public void shouldIncludeMultipleFiles() throws Exception {

        String[] includes = {"**/*example-1.raml", "**/*example-2.raml"};
        String[] excludes = {};
        Path baseDir = Paths.get("src/test/resources/raml/");

        FileTreeScanner fileTreeResolver = new FileTreeScanner();

        Collection<Path> paths = fileTreeResolver.find(baseDir, includes, excludes);

        assertThat(paths, hasSize(2));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("example-2.raml"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldIncludeRamlFilesByDefaultIfNoIncludesSpecified() throws Exception {

        String[] includes = {};
        String[] excludes = {};
        Path baseDir = Paths.get("src/test/resources/raml/");

        FileTreeScanner fileTreeResolver = new FileTreeScanner();

        Collection<Path> paths = fileTreeResolver.find(baseDir, includes, excludes);

        assertThat(paths, hasSize(4));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("ignore.raml")),
                equalTo(Paths.get("subfolder/example-3.raml")),
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("example-2.raml"))));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldExcludeMultipleFiles() throws Exception {

        String[] includes = {"**/*raml"};
        String[] excludes = {"**/*ignore.raml", "**/*example-2.raml"};
        Path baseDir = Paths.get("src/test/resources/raml/");

        FileTreeScanner fileTreeResolver = new FileTreeScanner();

        Collection<Path> paths = fileTreeResolver.find(baseDir, includes, excludes);

        assertThat(paths, hasSize(2));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("subfolder/example-3.raml"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnEmptyCollectionIfBaseDirDoesNotExist() throws Exception {

        String[] includes = {"**/*.messaging.raml"};
        String[] excludes = {"**/*external-ignore.raml"};
        Path baseDir = Paths.get("C:\\workspace-moj\\raml-maven\\raml-maven-plugin-it\\src\\raml");

        FileTreeScanner fileTreeResolver = new FileTreeScanner();

        Collection<Path> paths = fileTreeResolver.find(baseDir, includes, excludes);

        assertThat(paths, empty());
    }


}
