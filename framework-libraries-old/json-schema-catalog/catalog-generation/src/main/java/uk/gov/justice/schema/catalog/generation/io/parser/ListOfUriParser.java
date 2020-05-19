package uk.gov.justice.schema.catalog.generation.io.parser;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;

import java.net.URI;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Used by the Maven Generator Plugin to process the file paths set in the Maven configuration. This
 * parser transforms all the paths in to absolute URIs and returns as a Collection of List of URI.
 * The Maven Generator will be supplied with a single call with a List of URIs.
 */
public class ListOfUriParser implements FileParser<List<URI>> {

    /**
     * Returns all the files as URIs, under the given base path specified by the Collection
     *
     * Used by the Maven Generator Plugin
     *
     * @param basePath The base path of the source directories
     * @param paths    Appended to the base path to create the source directories
     * @return A list of source URIs
     */
    @Override
    public Collection<List<URI>> parse(final Path basePath, final Collection<Path> paths) {
        final Function<Path, URI> toUri = uriFunctionWith(basePath);

        return singletonList(paths.stream()
                .map(toUri)
                .collect(toList()));
    }

    private Function<Path, URI> uriFunctionWith(final Path basePath) {
        return path -> basePath.resolve(path).toAbsolutePath().toUri();
    }
}
