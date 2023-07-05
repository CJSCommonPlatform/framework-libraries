package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class ResourceLoaderFactoryTest {

    @Test
    public void shouldReturnFileResourceLoader() throws Exception {
        final Path basePath = get("src/test/resources/parser/");

        final ResourceLoader resourceLoader = new ResourceLoaderFactory().resourceLoaderFor(basePath);

        assertThat(resourceLoader, is(instanceOf(FileResourceLoader.class)));
    }

    @Test
    public void shouldReturnClassPathResourceLoader() throws Exception {
        final Path basePath = get("CLASSPATH");

        final ResourceLoader resourceLoader = new ResourceLoaderFactory().resourceLoaderFor(basePath);

        assertThat(resourceLoader, is(instanceOf(ClasspathResourceLoader.class)));
    }
}