package uk.gov.justice.schema.catalog;

import static java.util.stream.Collectors.toMap;

import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

public class JsonSchemaLoader {

    private final FileContentsAsStringLoader fileContentsAsStringLoader;

    @Inject
    public JsonSchemaLoader(final FileContentsAsStringLoader fileContentsAsStringLoader) {
        this.fileContentsAsStringLoader = fileContentsAsStringLoader;
    }

    public Map<String, String> loadJsonFrom(final Map<String, URL> schemaLocationMap) {
        return schemaLocationMap.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> fileContentsAsStringLoader.readFileContents(entry.getValue())));
    }
}
