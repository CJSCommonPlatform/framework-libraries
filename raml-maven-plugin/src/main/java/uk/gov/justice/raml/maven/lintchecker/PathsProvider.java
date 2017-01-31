package uk.gov.justice.raml.maven.lintchecker;

import static java.lang.String.format;

import uk.gov.justice.raml.io.FileTreeScannerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import org.apache.maven.plugin.MojoFailureException;

public class PathsProvider {

    private final FileTreeScannerFactory scannerFactory;

    public PathsProvider() {
        this(new FileTreeScannerFactory());
    }

    @VisibleForTesting
    PathsProvider(final FileTreeScannerFactory scannerFactory) {
        this.scannerFactory = scannerFactory;
    }

    public Collection<Path> getPaths(final File sourceDirectory, final List<String> includes, final List<String> excludes) throws MojoFailureException {
        try {
            return scannerFactory.create().find(
                    sourceDirectory.toPath(),
                    toArray(includes),
                    toArray(excludes));
        } catch (final IOException e) {
            throw new MojoFailureException(format("Failed to find paths for source directory %s", sourceDirectory.toString()), e);
        }
    }

    private String[] toArray(final List<String> list) {
        return list.toArray(new String[list.size()]);
    }
}
