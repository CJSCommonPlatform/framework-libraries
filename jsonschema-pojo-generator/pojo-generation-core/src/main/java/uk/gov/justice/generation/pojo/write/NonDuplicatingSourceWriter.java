package uk.gov.justice.generation.pojo.write;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.File;

import org.slf4j.Logger;

/**
 * Writes the generated java POJO source file.
 *
 * Before writing the file system checks to see if a file with the name of the new class does not
 * already exist and if so will skip the generation
 */
public class NonDuplicatingSourceWriter {

    private final JavaSourceFileProvider javaSourceFileProvider;
    private final SourceWriter sourceWriter;

    public NonDuplicatingSourceWriter(final JavaSourceFileProvider javaSourceFileProvider, final SourceWriter sourceWriter) {
        this.javaSourceFileProvider = javaSourceFileProvider;
        this.sourceWriter = sourceWriter;
    }

    /**
     * Writes the generated java file to disk at the correct output path. Will skip writing any file
     * that already exists
     *
     * @param classGeneratable  The {@link ClassGeneratable} that specifeis the class to be written
     * @param generationContext The global {@link GenerationContext}
     * @return The written file
     */
    public File write(final ClassGeneratable classGeneratable, final GenerationContext generationContext) {

        final Logger logger = generationContext.getLoggerFor(getClass());
        final File sourceFile = javaSourceFileProvider.getJavaFile(generationContext, classGeneratable);

        if (!sourceFile.exists()) {
            sourceWriter.write(classGeneratable, generationContext);

            if (sourceFile.exists()) {
                logger.info("Wrote new Java file '{}'", sourceFile.getName());
            } else {
                throw new SourceCodeWriteException(format("Failed to write java file '%s'", sourceFile.getAbsolutePath()));
            }
        } 

        return sourceFile;
    }
}
