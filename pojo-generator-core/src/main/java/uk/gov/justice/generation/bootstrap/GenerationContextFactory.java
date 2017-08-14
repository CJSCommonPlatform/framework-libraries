package uk.gov.justice.generation.bootstrap;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.write.SourceCodeWriteException;

import java.io.File;

public class GenerationContextFactory {

    public GenerationContext create(final File sourceRootDirectory) {

        if (! sourceRootDirectory.exists()) {
            throw new SourceCodeWriteException(format("Source code root directory '%s' does not exist", sourceRootDirectory.getAbsolutePath()));
        }

        if (! sourceRootDirectory.isDirectory()) {
            throw new SourceCodeWriteException(format("Source code root directory '%s' is not a directory", sourceRootDirectory.getAbsolutePath()));
        }

        if (! sourceRootDirectory.canWrite()) {
            throw new SourceCodeWriteException(format("Source code root directory '%s' is not writable", sourceRootDirectory.getAbsolutePath()));
        }

        return new GenerationContext(sourceRootDirectory);
    }
}
