package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class FileResourceLoaderTest {

    private final Path basePath = get("src/test/resources/parser/");

    @Test
    public void shouldReturnInputStreamOfFilePath() throws Exception {
        final Path filPpath = get("test1.txt");

        final InputStream inputStream = new FileResourceLoader().loadFrom(basePath, filPpath);

        final String fileContent = IOUtils.toString(inputStream);
        assertThat(fileContent, is("Test 1"));
    }

    @Test
    public void shouldThrowResourceNotFoundExcptionIfFileIsNotFound() throws Exception {
        final Path filPpath = get("unknown.txt");

        try {
            new FileResourceLoader().loadFrom(basePath, filPpath);
            fail();
        } catch (final ResourceNotFoundException ex) {
            assertThat(ex.getMessage(), containsString("Unable to load file: "));
            assertThat(ex.getMessage(), containsString("src/test/resources/parser/unknown.txt"));
            assertThat(ex.getCause(), is(instanceOf(FileNotFoundException.class)));
        }
    }
}