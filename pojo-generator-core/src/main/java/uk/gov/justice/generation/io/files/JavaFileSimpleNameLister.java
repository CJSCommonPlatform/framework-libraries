package uk.gov.justice.generation.io.files;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class JavaFileSimpleNameLister {

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
