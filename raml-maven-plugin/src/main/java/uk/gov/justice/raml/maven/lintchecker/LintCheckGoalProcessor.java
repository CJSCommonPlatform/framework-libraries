package uk.gov.justice.raml.maven.lintchecker;

import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.raml.model.Raml;

public class LintCheckGoalProcessor {

    private final RamlFileParser ramlFileParser;
    private final FileTreeScanner fileTreeScanner;

    public LintCheckGoalProcessor(
            final RamlFileParser ramlFileParser,
            final FileTreeScanner fileTreeScanner) {
        this.ramlFileParser = ramlFileParser;
        this.fileTreeScanner = fileTreeScanner;
    }

    public void execute(
            final File sourceDirectory,
            final List<LintCheckRule> rules,
            final List<String> includes,
            final List<String> excludes) throws MojoExecutionException {

        final Collection<Path> paths = getPaths(sourceDirectory, includes, excludes);
        final Collection<Raml> ramls = ramlFileParser.ramlOf(sourceDirectory.toPath(), paths);

        for (final Raml raml : ramls) {
            for (final LintCheckRule rule : rules) {
                try {
                    rule.execute(raml, new LintCheckConfiguration());
                } catch (final LintCheckerException e) {
                    throw new MojoExecutionException("Lint checker rule failed for rule " + rule.getClass().getSimpleName(), e);
                }
            }
        }
    }

    private Collection<Path> getPaths(
            final File sourceDirectory,
            final List<String> includes,
            final List<String> excludes) throws MojoExecutionException {
        try {
            return fileTreeScanner.find(sourceDirectory.toPath(), toArray(includes), toArray(excludes));
        } catch (final IOException e) {
            throw new MojoExecutionException("Failed to find paths from source directory " + sourceDirectory.getAbsolutePath());
        }
    }

    private String[] toArray(final List<String> list) {
        return list.toArray(new String[list.size()]);
    }
}
