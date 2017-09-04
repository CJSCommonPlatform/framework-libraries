package uk.gov.justice.generation.pojo.write;

import uk.gov.justice.generation.pojo.core.GenerationContext;

import java.io.File;

public class JavaSourceFileProvider {

    public File getJavaFile(final GenerationContext generationContext, final String className) {

        final String fileName = className + ".java";
        final String path = generationContext.getPackageName().replace('.', '/') + "/" + fileName;

        return generationContext.getOutputDirectoryPath().resolve(path).toFile();
    }
}
