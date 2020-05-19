package uk.gov.justice.raml.maven.lintchecker.processor;

import uk.gov.justice.raml.maven.lintchecker.rules.LintCheckRule;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public class LintCheckerGoalConfig {
    private final File sourceDirectory;
    private final List<LintCheckRule> rules;
    private final List<String> includes;
    private final List<String> excludes;
    private final MavenProject currentProject;
    private final Log log;

    public LintCheckerGoalConfig(final File sourceDirectory,
                                 final List<LintCheckRule> rules,
                                 final List<String> includes,
                                 final List<String> excludes,
                                 final MavenProject currentProject,
                                 final Log log) {
        this.sourceDirectory = sourceDirectory;
        this.rules = rules;
        this.includes = includes;
        this.excludes = excludes;
        this.currentProject = currentProject;
        this.log = log;
    }

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public List<LintCheckRule> getRules() {
        return rules;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public MavenProject getCurrentProject() {
        return currentProject;
    }

    public Log getLog() {
        return log;
    }
}
