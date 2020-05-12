package uk.gov.justice.generation.io.files;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class for finding {@code *.java} files in a list of specified locations
 * and returning a list of the java classes' simple names.
 *
 * This is used to search for hard coded pojos to allow for a user to write their own
 * version of a pojo and so not generate the java file for it.
 */
public class JavaFileSimpleNameLister {

    /**
     * Finds the list of all {@code *.java} files in the specified list of directories
     * and returns their simple names
     *
     * @param sourcePaths A list of {@link Path}s that are the source roots of *.java files
     * @param outputDirectory The output directory for generated files. Should be ignored in the search
     * @param packageName The package to convert to a path and append to the source paths
     * @return a list of the simple names of {@code *.java} files in the correct package
     */
    public List<String> findSimpleNames(final List<Path> sourcePaths, final Path outputDirectory, final String packageName) {

        final String packageAsPath = packageName.replace(".", "/");

        return sourcePaths
                .stream()
                .filter(sourcePath -> !sourcePath.equals(outputDirectory))
                .map(sourcePath -> sourcePath.resolve(packageAsPath))
                .flatMap(this::listFiles)
                .filter(file -> file.getName().endsWith(".java"))
                .map(this::getNameWithoutJavaExtension)
                .collect(toList());
    }

    private String getNameWithoutJavaExtension(final File file) {
        final String fileName = file.getName();
        return fileName.substring(0, fileName.length() - ".java".length());
    }

    private Stream<File> listFiles(final Path path) {
        final File[] values = path.toFile().listFiles();
        if (values == null) {
            return empty();
        }

        return Stream.of(values);
    }
}
