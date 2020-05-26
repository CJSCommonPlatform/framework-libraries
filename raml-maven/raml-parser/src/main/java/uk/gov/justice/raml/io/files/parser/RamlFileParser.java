package uk.gov.justice.raml.io.files.parser;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import org.raml.model.Raml;
import org.raml.parser.loader.ClassPathResourceLoader;
import org.raml.parser.loader.FileResourceLoader;
import org.raml.parser.loader.ResourceLoader;
import org.raml.parser.visitor.RamlDocumentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for parsing RAML files.
 */
public class RamlFileParser {

    private static final String CLASSPATH = "CLASSPATH";

    /**
     * Takes a collection of file paths, loads the files and parses them into a {@link Raml}
     * objects.
     *
     * @param baseDir the base directory to load the files from
     * @param paths   the files to ramlOf
     * @return the {@link Raml} models
     */
    public Collection<Raml> ramlOf(final Path baseDir, final Collection<Path> paths) {
        return paths.stream()
                .map(path -> ramlOf(resourceLoaderOf(baseDir), filePathOf(baseDir, path)))
                .collect(Collectors.toList());
    }

    private String filePathOf(final Path baseDir, final Path path) {
        return baseDir.endsWith(CLASSPATH) ? path.toString().replace("\\", "/") : path.toFile().getName();
    }


    private ResourceLoader resourceLoaderOf(final Path baseDir) {
        return baseDir.endsWith(CLASSPATH) ? new ClassPathResourceLoader() : new FileResourceLoader(baseDir.toFile());
    }

    private Raml ramlOf(final ResourceLoader loader, final String filePath) {
        return new RamlDocumentBuilder(loader)
                .build(filePath);
    }
}
