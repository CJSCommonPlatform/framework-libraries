package uk.gov.justice.raml.maven.lintchecker;

import static java.lang.String.format;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.raml.model.Raml;

public class LintCheckerMojoDelegate {

    private final RamlProvider ramlProvider;
    private final PathsProvider pathsProvider;

    public LintCheckerMojoDelegate() {
        this(new RamlProvider(), new PathsProvider());
    }

    @VisibleForTesting
    LintCheckerMojoDelegate(final RamlProvider ramlProvider, final PathsProvider pathsProvider) {
        this.ramlProvider = ramlProvider;
        this.pathsProvider = pathsProvider;
    }

    public void execute(
            final File sourceDirectory,
            final List<LintCheckRule> rules,
            final List<String> includes,
            final List<String> excludes) throws MojoExecutionException, MojoFailureException {

        final Collection<Path> paths = pathsProvider.getPaths(sourceDirectory, includes, excludes);
        final Collection<Raml> ramls = ramlProvider.getRamls(sourceDirectory, paths);

        for (final Raml raml : ramls) {
            for (final LintCheckRule rule : rules) {
                try {
                    rule.execute(raml);
                } catch (final LintCheckerException e) {
                    throw new MojoExecutionException("Lint checker rule failed for rule " + rule.getClass().getSimpleName(), e);
                }
            }
        }
    }
}
