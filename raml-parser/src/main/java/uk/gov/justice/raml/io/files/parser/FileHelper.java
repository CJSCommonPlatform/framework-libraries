package uk.gov.justice.raml.io.files.parser;

import java.nio.file.Path;

import org.raml.model.Raml;
import org.raml.parser.loader.ClassPathResourceLoader;
import org.raml.parser.loader.FileResourceLoader;
import org.raml.parser.loader.ResourceLoader;
import org.raml.parser.visitor.RamlDocumentBuilder;

public class FileHelper {
    static final String CLASSPATH = "CLASSPATH";

    public FileHelper() {
    }

    String filePathOf(final Path baseDir, final Path path) {
        return baseDir.endsWith(CLASSPATH) ? path.toString().replace("\\", "/") : path.toFile().getName();
    }

    ResourceLoader resourceLoaderOf(final Path baseDir) {
        return baseDir.endsWith(CLASSPATH) ? new ClassPathResourceLoader() : new FileResourceLoader(baseDir.toFile());
    }
}