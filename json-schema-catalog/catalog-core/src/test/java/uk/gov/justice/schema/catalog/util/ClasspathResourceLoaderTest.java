package uk.gov.justice.schema.catalog.util;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ClasspathResourceLoaderTest {

    private final ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();

    @Test
    public void shouldLoadResourcesFromTheClasspath() throws Exception {

        final String filename = "json/random-classpath-resource.txt";
        final List<URL> resources = classpathResourceLoader.getResources(filename);

        assertThat(resources.size(), is(1));
        assertThat(resources.get(0).toString(), endsWith(filename));
    }
}
