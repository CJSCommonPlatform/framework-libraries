package uk.gov.justice.schema.catalog.util;

import static java.util.Collections.list;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class ClasspathResourceLoader {

    public ClasspathResourceLoader(final UrlConverter urlConverter) {
        this.urlConverter = urlConverter;
    }

    private final UrlConverter urlConverter;


    public List<URI> getResources(final Class<?> clazz, final String location) throws IOException {
        final List<URL> urls = list(clazz.getClassLoader().getResources(location));
        return urls.stream().map(urlConverter::toUri).collect(toList());
    }
}
