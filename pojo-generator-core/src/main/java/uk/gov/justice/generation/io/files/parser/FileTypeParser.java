package uk.gov.justice.generation.io.files.parser;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;

public class FileTypeParser implements FileParser<File> {

    @Override
    public Collection<File> parse(final Path basePath, final Collection<Path> paths) {
        final Function<Path, File> toFile = fileFunctionWith(basePath);
        return paths.stream()
                .map(toFile)
                .collect(toList());
    }

    private Function<Path, File> fileFunctionWith(final Path basePath) {
        return path -> basePath.resolve(path).toAbsolutePath().toFile();
    }
}