package uk.gov.justice.generation.pojo.integration.utils;

import javax.inject.Inject;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;

/**
 * Utility for loading JSON schemas.
 */
public class JsonSchemaLoader {

    @Inject
    private Logger logger;

    /**
     * Locate a JSON schema file on the classpath and load it.
     * @param path the path to the JSON schema file
     * @return the schema
     */
    public static Schema loadSchema(final String path) {

        // TODO: load from classpath rather than working directory
        final JSONObject schemaJsonObject = new JSONObject(new FileLoader().loadAsJsonSting(path));
        return SchemaLoader.load(schemaJsonObject);
    }

    public static ArraySchema loadArraySchema(final String path) {
        return (ArraySchema) loadSchema(path);
    }
}
