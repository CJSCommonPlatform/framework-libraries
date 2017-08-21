package uk.gov.justice.generation.bootstrap;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.write.SourceCodeWriteException;

import java.io.File;
import java.nio.file.Path;

public class GenerationContextFactory {

    public GenerationContext createWith(final Path outputDirectoryPath) {

        final File directory = outputDirectoryPath.toFile();

        if (!directory.exists()) {
            throw new SourceCodeWriteException(format("Source code root directory '%s' does not exist", outputDirectoryPath.toAbsolutePath()));
        }

        if (!directory.isDirectory()) {
            throw new SourceCodeWriteException(format("Source code root directory '%s' is not a directory", outputDirectoryPath.toAbsolutePath()));
        }

        if (!directory.canWrite()) {
            throw new SourceCodeWriteException(format("Source code root directory '%s' is not writable", outputDirectoryPath.toAbsolutePath()));
        }

        return new GenerationContext(outputDirectoryPath);
    }
}
