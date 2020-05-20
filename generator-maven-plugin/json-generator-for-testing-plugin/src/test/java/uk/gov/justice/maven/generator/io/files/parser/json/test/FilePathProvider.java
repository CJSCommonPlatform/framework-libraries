package uk.gov.justice.maven.generator.io.files.parser.json.test;

import static java.lang.String.format;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathProvider {

    public static final String FILE_NAME = "json-titles.txt";

    public Path filePath(final GeneratorConfig generatorConfig) {
        return Paths.get(format("%s/%s", generatorConfig.getOutputDirectory().toString(), FILE_NAME));
    }
}
