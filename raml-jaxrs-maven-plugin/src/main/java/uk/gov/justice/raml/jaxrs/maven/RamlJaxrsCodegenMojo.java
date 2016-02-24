package uk.gov.justice.raml.jaxrs.maven;

import static java.text.MessageFormat.format;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import uk.gov.justice.raml.jaxrs.core.Configuration;
import uk.gov.justice.raml.jaxrs.core.DefaultGenerator;
import uk.gov.justice.raml.jaxrs.core.Generator;

@Mojo(name = "generate", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class RamlJaxrsCodegenMojo extends AbstractMojo {

    private static final String RAML_HEADER = "#%RAML";
    private static final String RAML_EXTENSION = "raml";
    private static final String FAILED_TO_CLEAN_DIRECTORY = "Failed to clean directory: {0}";
    private static final String GENERATING_JAVA_CLASSES = "Generating Java classes from: {0}";
    private static final String FAILED_TO_CREATE_DIRECTORY = "Failed to create directory: {0}";
    private static final String LOOKING_FOR_RAML_FILES = "Looking for RAML files in and below: {0}";
    private static final String SOURCE_DIRECTORY_MUST_BE_PROVIDED = "SourceDirectory must be provided";
    private static final String ERROR_GENERATING_JAVA_CLASSES = "Error generating Java classes from: {0}";
    private static final String THE_PROVIDED_PATH_DOESN_T_REFER_TO_A_VALID_DIRECTORY = "The provided path doesn''t refer to a valid directory: {0}";
    private static final String DOES_NOT_SEEM_TO_BE_RAML_ROOT_FILE = "{0} does not seem to be RAML root file -skipped(first line should start from #%RAML 0.8";

    private final Generator generator;


    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    /**
     * Target directory for generated Java source files.
     */
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources/raml-jaxrs")
    private File outputDirectory;

    /**
     * Directory location of the RAML file(s).
     */
    @Parameter(property = "sourceDirectory", defaultValue = "${basedir}/src/main/raml")
    private File sourceDirectory;

    /**
     * Base package name used for generated Java classes.
     */
    @Parameter(property = "basePackageName", required = true)
    private String basePackageName;


    public RamlJaxrsCodegenMojo() {
        this(new DefaultGenerator());
    }

    RamlJaxrsCodegenMojo(Generator generator) {
        this.generator = generator;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        verify(sourceDirectory);
        prepare(outputDirectory);
        project.addCompileSourceRoot(outputDirectory.getPath());
        process(ramlFiles());
    }

    private void process(Collection<File> ramlFiles) throws MojoExecutionException {
        File currentSourcePath = null;

        try {
            for (final File ramlFile : ramlFiles) {
                currentSourcePath = ramlFile;
                String raml = FileUtils.readFileToString(ramlFile);
                if (raml != null && raml.startsWith(RAML_HEADER)) {
                    getLog().info(format(GENERATING_JAVA_CLASSES, ramlFile));
                    generator.run(raml, configuration());
                } else {
                    getLog().info(format(DOES_NOT_SEEM_TO_BE_RAML_ROOT_FILE, ramlFile));
                }
            }
        } catch (final Exception e) {
            throw new MojoExecutionException(format(ERROR_GENERATING_JAVA_CLASSES, currentSourcePath), e);
        }
    }

    private Configuration configuration() {
        final Configuration configuration = new Configuration();

        configuration.setBasePackageName(basePackageName);
        configuration.setOutputDirectory(outputDirectory);
        configuration.setSourceDirectory(sourceDirectory);
        return configuration;
    }

    private void verify(File sourceDirectory2) throws MojoExecutionException {
        if (sourceDirectory2 == null) {
            throw new MojoExecutionException(SOURCE_DIRECTORY_MUST_BE_PROVIDED);
        }
    }

    private void prepare(File outputDirectory) throws MojoExecutionException {
        try {
            FileUtils.forceMkdir(outputDirectory);
        } catch (final IOException ioe) {
            throw new MojoExecutionException(format(FAILED_TO_CREATE_DIRECTORY, outputDirectory), ioe);
        }

        try {
            FileUtils.cleanDirectory(outputDirectory);
        } catch (final IOException ioe) {
            throw new MojoExecutionException(format(FAILED_TO_CLEAN_DIRECTORY, outputDirectory), ioe);
        }
    }

    private Collection<File> ramlFiles() throws MojoExecutionException {

        if (!sourceDirectory.isDirectory()) {
            throw new MojoExecutionException(
                    format(THE_PROVIDED_PATH_DOESN_T_REFER_TO_A_VALID_DIRECTORY, sourceDirectory));
        }

        getLog().info(format(LOOKING_FOR_RAML_FILES, sourceDirectory));

        return FileUtils.listFiles(sourceDirectory, new String[] { RAML_EXTENSION }, true);
    }

    void setSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    void setProject(MavenProject project) {
        this.project = project;
    }

    void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }
}
