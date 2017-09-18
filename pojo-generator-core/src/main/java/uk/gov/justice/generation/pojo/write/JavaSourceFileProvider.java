package uk.gov.justice.generation.pojo.write;

import uk.gov.justice.generation.pojo.core.GenerationContext;

import java.io.File;

/**
 * Gets the java source file for a class with the specified name and global package
 * name in any of our specified source directories
 */
public class JavaSourceFileProvider {

    /**
     * Gets the java source {@link File} with the specified name. NB this file may
     * not exist
     *
     * @param generationContext The global {@link GenerationContext}
     * @param className The class name of the java file we are looking for
     * @return a {@link File} that may or may not exist
     */
    public File getJavaFile(final GenerationContext generationContext, final String className) {

        final String fileName = className + ".java";
        final String path = generationContext.getPackageName().replace('.', '/') + "/" + fileName;

        return generationContext.getOutputDirectoryPath().resolve(path).toFile();
    }
}
