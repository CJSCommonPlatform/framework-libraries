package uk.gov.justice.generation.pojo.write;

import uk.gov.justice.generation.pojo.dom.ClassName;

import java.io.File;

public class JavaSourceFileProvider {

    public File getJavaFile(final File rootOutputDirectory, final ClassName className) {

        final String fileName = className.getSimpleName() + ".java";
        final String path = className.getPackageName().replace('.', '/') + "/" + fileName;

        return new File(rootOutputDirectory, path);
    }
}
