package uk.gov.justice.maven.generator.io.files.parser;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import org.raml.model.Raml;
import org.raml.parser.loader.ResourceLoader;
import org.raml.parser.visitor.RamlDocumentBuilder;

/**
 * Utility class for parsing RAML files.
 */
public class RamlFileParser implements FileParser<Raml> {

    private final FileHelper fileHelper = new FileHelper();

    /**
     * Takes a collection of file paths, loads the files and parses them into a {@link Raml}
     * objects.
     *
     * @param baseDir the base directory to load the files from
     * @param paths   the files to parse
     * @return the {@link Raml} models
     */
    public Collection<Raml> parse(final Path baseDir, final Collection<Path> paths) {
        return paths.stream()
                .map(path -> parse(fileHelper.resourceLoaderOf(baseDir), fileHelper.filePathOf(baseDir, path)))
                .collect(Collectors.toList());
    }

    private Raml parse(final ResourceLoader loader, final String filePath) {
        return new RamlDocumentBuilder(loader)
                .build(filePath);
    }
}
