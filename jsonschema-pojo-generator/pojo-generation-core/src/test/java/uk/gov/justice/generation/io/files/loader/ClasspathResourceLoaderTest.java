package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ClasspathResourceLoaderTest {

    @Test
    public void shouldReturnInputStreamOfClasspathResource() throws Exception {
        final Path basePath = get("CLASSPATH");
        final Path path = get("parser/test1.txt");

        final InputStream inputStream = new ClasspathResourceLoader().loadFrom(basePath, path);

        final String resourceContent = IOUtils.toString(inputStream);
        assertThat(resourceContent, is("Test 1"));
    }

    @Test
    public void shouldThrowResourceNotFoundExcptionIfResourceIsNotFound() throws Exception {
        final Path basePath = get("CLASSPATH");
        final Path path = get("parser/unknown.txt");

        try {
            new ClasspathResourceLoader().loadFrom(basePath, path);
        } catch (final ResourceNotFoundException ex) {
            assertThat(ex.getMessage(), containsString("Unable to load resource: "));
            assertThat(ex.getMessage(), containsString("parser/unknown.txt"));
        }
    }
}