package uk.gov.justice.maven.generator.io.files.parser.generator.parser;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;

import java.nio.file.Path;
import java.util.Collection;

public class TestFileParser implements FileParser<String> {

    @Override
    public Collection<String> parse(final Path baseDir, final Collection<Path> paths) {
        return null;
    }
}