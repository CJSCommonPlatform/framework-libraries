package uk.gov.justice.maven.generator.io.files.parser.json.test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Collections.singletonList;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.json.JsonObject;

/**
 * Generator for testing - JSON titles are appended to a text file.
 */
public class JsonTitleAppendingGenerator implements Generator<JsonObject> {

    private final FilePathProvider filePathProvider;

    public JsonTitleAppendingGenerator(final FilePathProvider filePathProvider) {
        this.filePathProvider = filePathProvider;
    }

    @Override
    public void run(final JsonObject jsonObject, final GeneratorConfig generatorConfig) {
        try {
            System.out.println("Entered JsonTitleAppendingGenerator");
            final Path filePath = filePathProvider.filePath(generatorConfig);

            Files.write(filePath, singletonList(jsonObject.getString("value")), UTF_8, APPEND, CREATE);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
