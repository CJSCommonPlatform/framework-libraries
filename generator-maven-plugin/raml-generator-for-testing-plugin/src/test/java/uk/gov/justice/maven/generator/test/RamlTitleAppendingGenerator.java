package uk.gov.justice.maven.generator.test;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Arrays.asList;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.raml.model.Raml;

/**
 * Generator for testing - RAML titles are appended to a text file.
 */
public class RamlTitleAppendingGenerator implements Generator<Raml> {
    public static final String FILE_NAME = "raml-titles.txt";

    @Override
    public void run(final Raml raml, final GeneratorConfig generatorConfig) {
        try {
            Files.write(filePath(generatorConfig), asList(raml.getTitle()), UTF_8, APPEND, CREATE);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Path filePath(final GeneratorConfig generatorConfig) {
        return Paths.get(format("%s/%s", generatorConfig.getOutputDirectory().toString(), FILE_NAME));
    }

}
