package uk.gov.justice.raml.io.files.parser;

import javax.json.JsonObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.json.Json.createReader;


public class JsonSchemaFileParser implements FileParser<JsonObject> {

    @Override
    public Collection<JsonObject> parse(Path baseDir, Collection<Path> paths) {
        return paths.stream()
                .map(path -> read(baseDir.resolve(path).toAbsolutePath().toString()))
                .collect(Collectors.toList());
    }

    private JsonObject read(final String filePath) {
        try {
            return createReader(new FileReader(filePath)).readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
