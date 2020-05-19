package uk.gov.justice.maven.generator.io.files.parser;

import java.nio.file.Path;
import java.util.Collection;

public interface FileParser<T> {

    Collection<T> parse(final Path baseDir, final Collection<Path> paths);

}
