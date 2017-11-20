package uk.gov.justice.schema.catalog;

import static java.util.stream.Collectors.toMap;

import java.util.Map;

import javax.inject.Inject;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;

public class SchemaResolverAndLoader {

    private final JsonStringToSchemaConverter jsonStringToSchemaConverter;

    @Inject
    public SchemaResolverAndLoader(final JsonStringToSchemaConverter jsonStringToSchemaConverter) {
        this.jsonStringToSchemaConverter = jsonStringToSchemaConverter;
    }

    public Map<String, Schema> loadSchemas(final Map<String, String> urlsToJson, final SchemaClient schemaClient) {

        return urlsToJson.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> jsonStringToSchemaConverter.convert(entry.getValue(), schemaClient)));
    }

}
