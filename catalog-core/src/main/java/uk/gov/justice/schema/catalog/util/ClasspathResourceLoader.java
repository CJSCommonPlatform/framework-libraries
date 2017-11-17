package uk.gov.justice.schema.catalog.util;

import static java.util.Collections.list;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ClasspathResourceLoader {

    public List<URL> getResources(final Class<?> clazz, final String location) throws IOException {
        return list(clazz.getClassLoader().getResources(location));
    }
}
