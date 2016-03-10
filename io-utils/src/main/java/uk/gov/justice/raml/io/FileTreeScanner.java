package uk.gov.justice.raml.io;

import org.codehaus.plexus.util.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Utility class for searching directories.
 */
public class FileTreeScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTreeScanner.class);

    /**
     * Finding all files within a directory that fulfil a set of include and exclude patterns, using standard
     * Ant patterns - {@see http://ant.apache.org/manual/dirtasks.html#patterns}.
     *
     * @param baseDir  the path to search under
     * @param includes the path patterns to include
     * @param excludes the path patterns to exclude
     * @return a list of paths to matching files under the specified directory
     */
    public Collection<Path> find(final Path baseDir, final String[] includes, final String[] excludes) throws IOException {

        LOGGER.debug(format("Finding files in: %s", baseDir.toString()));

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(baseDir.toFile());
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();

        return Arrays.asList(scanner.getIncludedFiles())
                .stream()
                .map(Paths::get)
                .collect(Collectors.toList());
    }
}
