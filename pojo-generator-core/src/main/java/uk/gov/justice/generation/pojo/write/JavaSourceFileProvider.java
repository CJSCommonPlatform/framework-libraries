package uk.gov.justice.generation.pojo.write;

import uk.gov.justice.generation.pojo.dom.ClassName;

import java.io.File;
import java.nio.file.Path;

public class JavaSourceFileProvider {

    public File getJavaFile(final Path rootOutputDirectory, final ClassName className) {

        final String fileName = className.getSimpleName() + ".java";
        final String path = className.getPackageName().replace('.', '/') + "/" + fileName;

        return rootOutputDirectory.resolve(path).toFile();
    }
}
