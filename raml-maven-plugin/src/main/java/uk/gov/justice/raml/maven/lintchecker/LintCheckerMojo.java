package uk.gov.justice.raml.maven.lintchecker;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import uk.gov.justice.raml.maven.common.BasicMojo;

import java.nio.file.Path;
import java.util.Collection;

import com.google.common.annotations.VisibleForTesting;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.raml.model.Raml;


@Mojo(name = "lint-check")
public class LintCheckerMojo extends BasicMojo {

    private final LintCheckerMojoDelegate lintCheckerMojoDelegate = new LintCheckerMojoDelegate();

    /**
     * Array of objects that implement the LintCheckerRule interface to execute.
     */
    @SuppressWarnings("unused")
    @Parameter(required = true)
    protected LintCheckRule[] rules;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        lintCheckerMojoDelegate.execute(
                sourceDirectory,
                asList(rules),
                includes,
                excludes
        );
    }

}
