package uk.gov.justice.generation.pojo.integration.utils;

import java.io.File;

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
     * @param clazz The type of the Schema. Must extend org.everit.json.schema.Schema.
     * @return the schema
     */
    @SuppressWarnings("unchecked")
    public static <T extends Schema> T loadSchema(final File path, @SuppressWarnings("unused") final Class<T> clazz) {

        // TODO: load from classpath rather than working directory
        final JSONObject schemaJsonObject = new JSONObject(new FileLoader().loadAsJsonString(path));
        return (T) SchemaLoader.load(schemaJsonObject);
    }
}
