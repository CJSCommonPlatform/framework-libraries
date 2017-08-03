package uk.gov.justice.raml.maven.lintchecker.processor;

import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;
import uk.gov.justice.raml.maven.lintchecker.LintCheckConfiguration;
import uk.gov.justice.raml.maven.lintchecker.LintCheckerException;
import uk.gov.justice.raml.maven.lintchecker.rules.LintCheckRule;

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

    public void execute(final LintCheckerGoalConfig lintCheckerGoalConfig) throws MojoExecutionException {

        final Collection<Path> paths = getPaths(lintCheckerGoalConfig.getSourceDirectory(), lintCheckerGoalConfig.getIncludes(), lintCheckerGoalConfig.getExcludes());
        final Collection<Raml> ramls = ramlFileParser.parse(lintCheckerGoalConfig.getSourceDirectory().toPath(), paths);

        for (final Raml raml : ramls) {
            for (final LintCheckRule rule : lintCheckerGoalConfig.getRules()) {
                try {
                    rule.execute(raml, new LintCheckConfiguration(lintCheckerGoalConfig.getCurrentProject(),
                            lintCheckerGoalConfig.getLog()));
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
