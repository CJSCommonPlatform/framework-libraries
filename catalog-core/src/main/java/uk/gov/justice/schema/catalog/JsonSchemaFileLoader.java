package uk.gov.justice.schema.catalog;

import static java.util.stream.Collectors.toMap;

import java.util.Map;

public class JsonSchemaFileLoader {

    private final FileContentsAsStringLoader fileContentsAsStringLoader;
    private final CatalogToSchemaResolver catalogToSchemaResolver;

    public JsonSchemaFileLoader(final FileContentsAsStringLoader fileContentsAsStringLoader, final CatalogToSchemaResolver catalogToSchemaResolver) {
        this.fileContentsAsStringLoader = fileContentsAsStringLoader;
        this.catalogToSchemaResolver = catalogToSchemaResolver;
    }

    public Map<String, String> loadSchemas() {

        return catalogToSchemaResolver.resolveSchemaLocations().entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> fileContentsAsStringLoader.readFileContents(entry.getValue())));
    }
}
