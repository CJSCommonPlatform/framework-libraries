package uk.gov.justice.raml.jaxrs.maven;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

@Mojo(name = "generate", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateMojo extends AbstractMojo {

    /**
     * The fully qualified classname for the generator to use
     */
    @Parameter(property = "generatorName", required = true)
    private String generatorName;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    /**
     * Target directory for generated Java source files.
     */
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
    private File outputDirectory;

    /**
     * Directory location of the RAML file(s).
     */
    @Parameter(property = "sourceDirectory", defaultValue = "${basedir}/src/raml")
    private File sourceDirectory;

    /**
     * Base package name used for generated Java classes.
     */
    @Parameter(property = "basePackageName", required = true)
    private String basePackageName;

    @Override
    public void execute() throws MojoExecutionException {

        project.addCompileSourceRoot(outputDirectory.getPath());
        project.addTestCompileSourceRoot(outputDirectory.getPath());

        try {
            FileUtils.forceMkdir(outputDirectory);
            FileUtils.cleanDirectory(outputDirectory);
            new GenerateGoalProcessor(new GeneratorFactory()).generate(configuration());
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to apply generator to raml", e);
        }
    }

    private GenerateGoalConfig configuration() {

        return new GenerateGoalConfig(generatorName,
                sourceDirectory.toPath(),
                outputDirectory.toPath(),
                basePackageName);

    }

}
