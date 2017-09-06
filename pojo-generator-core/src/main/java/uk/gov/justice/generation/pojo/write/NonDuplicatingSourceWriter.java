package uk.gov.justice.generation.pojo.write;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;

import java.io.File;

import org.slf4j.Logger;

public class NonDuplicatingSourceWriter {

    private final JavaSourceFileProvider javaSourceFileProvider;
    private final SourceWriter sourceWriter;

    public NonDuplicatingSourceWriter(final JavaSourceFileProvider javaSourceFileProvider, final SourceWriter sourceWriter) {
        this.javaSourceFileProvider = javaSourceFileProvider;
        this.sourceWriter = sourceWriter;
    }

    public File write(final ClassGeneratable classGeneratable, final GenerationContext generationContext) {

        final Logger logger = generationContext.getLoggerFor(getClass());
        final File sourceFile = javaSourceFileProvider.getJavaFile(
                generationContext,
                classGeneratable.getSimpleClassName());

        if (!sourceFile.exists()) {
            sourceWriter.write(classGeneratable, generationContext);

            if (sourceFile.exists()) {
                logger.info("Wrote new Java file '{}'", sourceFile.getName());
            } else {
                throw new SourceCodeWriteException(format("Failed to write java file '%s'", sourceFile.getAbsolutePath()));
            }
        } else {
            logger.info("Skipping generation, Java file already exists: '{}'", sourceFile.getAbsolutePath());
        }

        return sourceFile;
    }
}
