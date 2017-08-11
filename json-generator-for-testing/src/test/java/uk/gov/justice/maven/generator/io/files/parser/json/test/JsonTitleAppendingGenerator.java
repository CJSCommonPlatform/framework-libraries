package uk.gov.justice.maven.generator.io.files.parser.json.test;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import javax.json.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Arrays.asList;

/**
 * Generator for testing - JSON titles are appended to a text file.
 */
public class JsonTitleAppendingGenerator implements Generator<JsonObject> {
    public static final String FILE_NAME = "json-titles.txt";

    @Override
    public void run(final JsonObject jsonObject, final GeneratorConfig generatorConfig) {
        try {
            System.out.println("Entered JsonTitleAppendingGenerator");
            Files.write(filePath(generatorConfig), asList(jsonObject.getString("value")), UTF_8, APPEND, CREATE);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Path filePath(final GeneratorConfig generatorConfig) {
        return Paths.get(format("%s/%s", generatorConfig.getOutputDirectory().toString(), FILE_NAME));
    }

}