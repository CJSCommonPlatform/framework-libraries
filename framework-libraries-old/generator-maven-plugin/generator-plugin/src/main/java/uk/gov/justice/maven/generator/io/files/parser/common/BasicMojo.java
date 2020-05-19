package uk.gov.justice.maven.generator.io.files.parser.common;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class BasicMojo extends AbstractMojo {
    private static final String DEFAULT_INCLUDE = "**/*.raml";

    /**
     * Directory location of the RAML file(s).
     */
    @Parameter(property = "sourceDirectory", defaultValue = "CLASSPATH")
    protected File sourceDirectory;

    @Parameter(property = "includes.include")
    protected List<String> includes;

    @Parameter(property = "excludes.exclude")
    protected List<String> excludes;

    @Parameter(defaultValue = "${project}")
    protected MavenProject project;

    protected void configureDefaultFileIncludes() {
        if (includes.isEmpty()) {
            includes = ImmutableList.of(DEFAULT_INCLUDE);
        }
    }
}
