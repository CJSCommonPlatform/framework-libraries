package uk.gov.justice.raml.maven.lintchecker;

import uk.gov.justice.raml.maven.lintchecker.rules.LintCheckRule;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Contains any configuration needed by any of the {@link LintCheckRule}s. Currently this class
 * contains nothing, but to future proof the {@link LintCheckRule} interface we pass this class
 * in the execute() method. Add any configuration your rule may need here.
 */
public class LintCheckConfiguration {

    private final MavenProject mavenProject;
    private final Log log;

    public LintCheckConfiguration(final MavenProject mavenProject, final Log log) {
        this.mavenProject = mavenProject;
        this.log = log;
    }

    public MavenProject getMavenProject() {
        return mavenProject;
    }

    public Log getLog() {
        return log;
    }
}
