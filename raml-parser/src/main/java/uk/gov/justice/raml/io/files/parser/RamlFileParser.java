package uk.gov.justice.raml.io.files.parser;

import org.raml.model.Raml;
import org.raml.parser.loader.FileResourceLoader;
import org.raml.parser.visitor.RamlDocumentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Utility class for parsing RAML files.
 */
public class RamlFileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(RamlFileParser.class);

    /**
     * Takes a collection of file paths, loads the files and parses them into a {@link Raml} objects.
     *
     * @param baseDir the base directory to load the files from
     * @param paths   the files to parse
     * @return the {@link Raml} models
     */
    public Collection<Raml> parse(final Path baseDir, final Collection<Path> paths) {

        FileResourceLoader loader = new FileResourceLoader(baseDir.toFile());

        return paths.stream()
                .map(path -> parse(loader, path))
                .collect(Collectors.toList());
    }

    private Raml parse(final FileResourceLoader loader, Path path) {
        LOGGER.info(format("Loading file %s", path.toString()));
        return new RamlDocumentBuilder(loader)
                .build(path.toFile().getName());
    }
}
