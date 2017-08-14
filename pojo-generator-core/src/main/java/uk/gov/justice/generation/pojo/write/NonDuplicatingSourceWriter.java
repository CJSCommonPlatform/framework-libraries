package uk.gov.justice.generation.pojo.write;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGenerator;

import java.io.File;

import org.slf4j.Logger;

public class NonDuplicatingSourceWriter {

    private final JavaSourceFileProvider javaSourceFileProvider;
    private final SourceWriter sourceWriter;

    public NonDuplicatingSourceWriter(final JavaSourceFileProvider javaSourceFileProvider, final SourceWriter sourceWriter) {
        this.javaSourceFileProvider = javaSourceFileProvider;
        this.sourceWriter = sourceWriter;
    }

    public File write(final ClassGenerator classGenerator, final GenerationContext generationContext) {

        final File sourceFile = javaSourceFileProvider.getJavaFile(
                generationContext.getSourceRootDirectory(),
                classGenerator.getClassName());

        if (!sourceFile.exists()) {
            sourceWriter.write(classGenerator, generationContext);
            final Logger logger = generationContext.getLoggerFor(getClass());

            if (sourceFile.exists()) {
                logger.info(format("Wrote new Java file '%s'", sourceFile.getName()));
            } else {
                throw new SourceCodeWriteException(format("Failed to write java file '%s'", sourceFile.getAbsolutePath()));
            }
        }

        return sourceFile;
    }
}
