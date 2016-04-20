package uk.gov.justice.raml.jaxrs.maven;

import com.google.common.collect.ImmutableList;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import uk.gov.justice.raml.io.FileTreeScannerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

@Mojo(name = "generate", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateMojo extends AbstractMojo {

    private static final String DEFAULT_INCLUDE = "**/*.raml";

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

    @Parameter(property = "includes.include")
    private List<String> includes;

    @Parameter(property = "excludes.exclude")
    private List<String> excludes;

    /**
     * Base package name used for generated Java classes.
     */
    @Parameter(property = "basePackageName", required = true)
    private String basePackageName;

    @Parameter(property = "generatorProperties", required = false)
    private Map<String, String> generatorProperties;

    @Override
    public void execute() throws MojoExecutionException {

        if (includes.isEmpty()) {
            includes = ImmutableList.of(DEFAULT_INCLUDE);
        }

        project.addCompileSourceRoot(outputDirectory.getPath());
        project.addTestCompileSourceRoot(outputDirectory.getPath());

        try {
            FileUtils.forceMkdir(outputDirectory);
            new GenerateGoalProcessor(new GeneratorFactory(), new FileTreeScannerFactory())
                    .generate(configuration());
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to apply generator to raml", e);
        }
    }

    private GenerateGoalConfig configuration() {

        return new GenerateGoalConfig(generatorName,
                sourceDirectory.toPath(),
                outputDirectory.toPath(),
                basePackageName,
                includes,
                excludes,
                generatorProperties
        );

    }

}
