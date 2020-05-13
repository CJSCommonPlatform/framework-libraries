package uk.gov.justice.maven.generator.io.files.parser;

import java.nio.file.Path;

import org.raml.parser.loader.ClassPathResourceLoader;
import org.raml.parser.loader.FileResourceLoader;
import org.raml.parser.loader.ResourceLoader;

public class FileHelper {
    static final String CLASSPATH = "CLASSPATH";

    String filePathOf(final Path baseDir, final Path path) {
        return baseDir.endsWith(CLASSPATH) ? path.toString().replace("\\", "/") : path.toFile().getName();
    }

    ResourceLoader resourceLoaderOf(final Path baseDir) {
        return baseDir.endsWith(CLASSPATH) ? new ClassPathResourceLoader() : new FileResourceLoader(baseDir.toFile());
    }
}