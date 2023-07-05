package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class FileResourceLoaderTest {

    @Test
    public void shouldReturnInputStreamOfFilePath() throws Exception {
        final Path filePath = get("test1.txt");
        final URL resource = getClass().getResource("/parser/test1.txt");

        assertThat(resource, is(notNullValue()));

        final Path parent = get(resource.toURI()).getParent();

        final InputStream inputStream = new FileResourceLoader().loadFrom(parent, filePath);
        final String fileContent = IOUtils.toString(inputStream);
        assertThat(fileContent, is("Test 1"));
    }

    @Test
    public void shouldThrowResourceNotFoundExcptionIfFileIsNotFound() throws Exception {

        final URL resource = getClass().getResource("/parser/test1.txt");

        assertThat(resource, is(notNullValue()));

        final Path parent = get(resource.toURI()).getParent();
        final Path filePath = get("unknown.txt");

        try {
            new FileResourceLoader().loadFrom(parent, filePath);
            fail();
        } catch (final ResourceNotFoundException ex) {
            assertThat(ex.getMessage(), containsString("Unable to load file: "));
            assertThat(ex.getMessage(), containsString("/parser/unknown.txt"));
            assertThat(ex.getCause(), is(instanceOf(FileNotFoundException.class)));
        }
    }
}