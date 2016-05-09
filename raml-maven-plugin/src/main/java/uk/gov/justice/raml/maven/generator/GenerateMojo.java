package uk.gov.justice.raml.maven.generator;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import uk.gov.justice.raml.io.FileTreeScannerFactory;
import uk.gov.justice.raml.maven.common.BasicMojo;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate", requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateMojo extends BasicMojo {


    /**
     * The fully qualified classname for the generator to use
     */
    @Parameter(property = "generatorName", required = true)
    private String generatorName;

    /**
     * Target directory for generated Java source files.
     */
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources")
    private File outputDirectory;


    /**
     * Base package name used for generated Java classes.
     */
    @Parameter(property = "basePackageName", required = true)
    private String basePackageName;

    @Parameter(property = "generatorProperties", required = false)
    private Map<String, String> generatorProperties;

    @Override
    public void execute() throws MojoExecutionException {

        configureDefaultFileIncludes();

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
