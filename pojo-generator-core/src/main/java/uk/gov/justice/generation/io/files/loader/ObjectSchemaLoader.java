package uk.gov.justice.generation.io.files.loader;

import static java.lang.String.format;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

public class ObjectSchemaLoader {

    public ObjectSchema loadFrom(final File file) {
        final JSONObject schemaJsonObject = new JSONObject(loadAsJsonString(file));
        final Schema schema = SchemaLoader.load(schemaJsonObject);

        if (schema instanceof ObjectSchema) {
            return (ObjectSchema) schema;
        }

        throw new SchemaLoaderException(format("Base schema must be of type object in: %s", file.toPath()));
    }

    private String loadAsJsonString(final File file) {
        try (final FileReader reader = new FileReader(file)) {
            return IOUtils.toString(reader);
        } catch (IOException ioe) {
            throw new SchemaLoaderException(format("File failed to load: %s", file.toPath()), ioe);
        }
    }
}