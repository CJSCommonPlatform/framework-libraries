package uk.gov.justice.raml.maven.lintchecker.rules;

import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;
import uk.gov.justice.raml.maven.common.BasicMojo;
import uk.gov.justice.raml.maven.lintchecker.processor.LintCheckGoalProcessor;
import uk.gov.justice.raml.maven.lintchecker.processor.LintCheckerGoalConfig;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "raml-lint-check", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class LintCheckMojo extends BasicMojo {

    /**
     * Array of objects that implement the LintCheckerRule interface to execute.
     */
    @SuppressWarnings("unused")
    @Parameter(required = true)
    private List<LintCheckRule> rules;

    @Override
    public void execute() throws MojoExecutionException {

        configureDefaultFileIncludes();

        final LintCheckGoalProcessor lintCheckGoalProcessor = new LintCheckGoalProcessor(
                new RamlFileParser(),
                new FileTreeScanner());
        
        lintCheckGoalProcessor.execute(
                new LintCheckerGoalConfig(sourceDirectory, rules, includes, excludes, project, getLog()));
    }
}
