package uk.gov.justice.schema.catalog.util;

import static java.util.Collections.list;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Finds all URIs to all resources on the classpath specified by the resource name
 */
public class ClasspathResourceLoader {


    /**
     * @param clazz A class from which to get its classloader
     * @param resourceName The name of the resource on the classpath
     * @return a {@link URL} to the resource
     * @throws IOException If the get fails
     */
    public List<URL> getResources(final Class<?> clazz, final String resourceName) throws IOException {
        return list(clazz.getClassLoader().getResources(resourceName));
    }
}
