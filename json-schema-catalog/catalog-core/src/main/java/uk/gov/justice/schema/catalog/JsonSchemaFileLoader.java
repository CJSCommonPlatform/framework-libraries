package uk.gov.justice.schema.catalog;

import static java.util.stream.Collectors.toMap;

import java.util.Map;

/**
 * Loads all Json Schema files found on the classpath as a raw {@link String} mapped by the id
 * of that Schema
 */
public class JsonSchemaFileLoader {

    private final FileContentsAsStringLoader fileContentsAsStringLoader;
    private final CatalogToSchemaResolver catalogToSchemaResolver;

    public JsonSchemaFileLoader(
            final FileContentsAsStringLoader fileContentsAsStringLoader,
            final CatalogToSchemaResolver catalogToSchemaResolver) {

        this.fileContentsAsStringLoader = fileContentsAsStringLoader;
        this.catalogToSchemaResolver = catalogToSchemaResolver;
    }

    /**
     * Loads all Json Schema files found on the classpath as a raw {@link String} mapped by the id
     * of that Schema.
     *
     * @return A {@link Map} of Schemas as a raw {@link String} mapped to the
     *         id of each Schema
     */
    public Map<String, String> loadSchemas() {

        return catalogToSchemaResolver.resolveSchemaLocations().entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> fileContentsAsStringLoader.readFileContents(entry.getValue())));
    }
}
