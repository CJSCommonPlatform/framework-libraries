package uk.gov.justice.generation.pojo.integration.utils;

import static org.apache.commons.io.FileUtils.cleanDirectory;

import java.io.File;
import java.nio.file.Path;

public class OutputDirectories {

    private static final String DEFAULT_CLASSES_DIR = "./target/test-classes";

    private File sourceOutputDirectory;
    private File classesOutputDirectory;

    public void makeDirectories(final String sourceDir) throws Exception {
        makeDirectories(sourceDir, DEFAULT_CLASSES_DIR);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void makeDirectories(final String sourceDir, final String classesDir) throws Exception {
        sourceOutputDirectory = new File(sourceDir);
        classesOutputDirectory = new File(classesDir);

        sourceOutputDirectory.mkdirs();
        classesOutputDirectory.mkdirs();

        if (sourceOutputDirectory.exists()) {
            cleanDirectory(sourceOutputDirectory);
        }
    }

    public Path getSourceOutputDirectory() {
        return sourceOutputDirectory.toPath();
    }

    public Path getClassesOutputDirectory() {
        return classesOutputDirectory.toPath();
    }
}
