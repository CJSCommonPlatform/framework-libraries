package uk.gov.justice.raml.maven.lintchecker;

import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;
import uk.gov.justice.raml.maven.common.BasicMojo;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


@Mojo(name = "lint-check")
public class LintCheckMojo extends BasicMojo {

    private final RamlFileParser ramlFileParser = new RamlFileParser();
    private final FileTreeScanner fileTreeScanner = new FileTreeScanner();

    /**
     * Array of objects that implement the LintCheckerRule interface to execute.
     */
    @SuppressWarnings("unused")
    @Parameter(required = true)
    protected List<LintCheckRule> rules;


    @Override
    public void execute() throws MojoExecutionException {

        final LintCheckGoalProcessor lintCheckGoalProcessor = new LintCheckGoalProcessor(
                ramlFileParser,
                fileTreeScanner);
        
        lintCheckGoalProcessor.execute(
                sourceDirectory,
                rules,
                includes,
                excludes
        );
    }
}
