package uk.gov.justice.maven.generator.io.files.parser;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;


public class JsonSchemaFileParser implements FileParser<JsonObject> {

    public static final JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(null);

    @Override
    public Collection<JsonObject> parse(final Path baseDir, final Collection<Path> paths) {
        return paths.stream()
                .map(path -> read(baseDir.resolve(path).toAbsolutePath().toString()))
                .collect(Collectors.toList());
    }

    private JsonObject read(final String filePath) {
        try (final JsonReader jsonReader = getReader(filePath)) {
            return jsonReader.readObject();
        } catch (IOException e) {
            throw new JsonSchemaIOException("Failed to read schema file", e);
        }
    }

    private JsonReader getReader(final String filePath) throws FileNotFoundException {
        final FileReader fileReader = new FileReader(filePath);
        return jsonReaderFactory.createReader(fileReader);
    }
}
