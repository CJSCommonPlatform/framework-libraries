package uk.gov.justice.raml.maven.lintchecker;

import static java.lang.String.format;

import uk.gov.justice.raml.io.FileTreeScannerFactory;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;
import uk.gov.justice.raml.maven.common.BasicMojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.raml.model.Raml;


@Mojo(name = "lint-check")
public class LintCheckerMojo extends BasicMojo {

    /**
     * Array of objects that implement the LintCheckerRule interface to execute.
     */
    @SuppressWarnings("unused")
    @Parameter(required = true)
    private LintCheckRule[] rules;

    private final FileTreeScannerFactory scannerFactory = new FileTreeScannerFactory();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        executeRules(getPaths());
    }

    private Collection<Path> getPaths() throws MojoFailureException {
        try {
            final String[] includesArray = includes.toArray(new String[includes.size()]);
            final String[] excludesArray = excludes.toArray(new String[excludes.size()]);

            return scannerFactory.create().find(sourceDirectory.toPath(), includesArray, excludesArray);
        } catch (IOException e) {
            throw new MojoFailureException(format("source directory paths not correct %s", sourceDirectory.toString()), e);
        }
    }

    private Collection<Raml> getRamls(Collection<Path> paths) {
        return new RamlFileParser()
                .ramlOf(sourceDirectory.toPath(), paths);
    }

    private void executeRules(final Collection<Path> paths) throws MojoExecutionException {
        for (final Raml raml : getRamls(paths)) {
            for (final LintCheckRule rule : rules) {
                try {
                    rule.execute(raml);
                } catch (LintCheckerException e) {
                    throw new MojoExecutionException(format("Lint checker rule %s has failed",
                            rule.getClass().getSimpleName()), e);
                }
            }
        }
    }
}
