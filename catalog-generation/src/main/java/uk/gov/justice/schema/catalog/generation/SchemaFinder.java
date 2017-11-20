package uk.gov.justice.schema.catalog.generation;

import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

public class SchemaFinder {

    private static final String SCHEMA_PACKAGE = "raml.json.schema";

    private final UrlConverter urlConverter;

    public SchemaFinder(final UrlConverter urlConverter) {
        this.urlConverter = urlConverter;
    }

    public List<URI> listSchemas() {

        final Reflections reflections = new Reflections(SCHEMA_PACKAGE, new ResourcesScanner());
        final List<String> fileNames = new ArrayList<>(reflections.getResources(Pattern.compile(".*\\.json")));

        sort(fileNames);

        return fileNames.stream()
                .map(this::asUri)
                .collect(toList());
    }

    private URI asUri(final String filename) {
        final URL resource = getClass().getClassLoader().getResource(filename);
        return urlConverter.toUri(resource);
    }
}
