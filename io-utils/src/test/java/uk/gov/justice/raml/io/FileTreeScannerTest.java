package uk.gov.justice.raml.io;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

/**
 * Unit tests for the {@link FileTreeScanner} class.
 */
public class FileTreeScannerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindOnlyMatchingFiles() throws Exception {

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
}
