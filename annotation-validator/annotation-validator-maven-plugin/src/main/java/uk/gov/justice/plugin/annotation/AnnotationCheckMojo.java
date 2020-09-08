package uk.gov.justice.plugin.annotation;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Class.forName;
import static java.lang.Thread.currentThread;
import static uk.gov.justice.plugin.validator.AnnotationValidatorFactory.getValidator;

import uk.gov.justice.maven.annotation.domain.ValidationResult;
import uk.gov.justice.maven.annotation.validator.AnnotationValidator;
import uk.gov.justice.plugin.domain.ReportConfig;
import uk.gov.justice.plugin.report.AnnotationValidationReport;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "check-annotations", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class AnnotationCheckMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${project.baseDir}")
    private String projectBaseDirectory;

    /**
     * Array of strings denoting fully qualified name of classes used for annotation
     */
    @Parameter(property = "annotations.annotation")
    private List<String> annotations;

    /**
     * Boolean flag to control the generation of report per annotation class
     */
    @Parameter(property = "generateReport", defaultValue = "false")
    private boolean generateReport;

    /**
     * Boolean flag to enable failing the build on encountering validation errors
     */
    @Parameter(property = "failBuildOnError", defaultValue = "false")
    private boolean failBuildOnError;

    /**
     * Map property to support flexible configuration of providing additional parameters for
     * implementations of {@link uk.gov.justice.maven.annotation.validator.AnnotationValidator}
     */
    @Parameter(property = "validatorProperties")
    private Map<String, String> validatorProperties;

    /**
     * Boolean property to turn of plugins validation check
     */
    @Parameter(property = "validationSkip")
    private boolean validationSkip;

    private URLClassLoader classLoader;

    @Override
    public void execute() throws MojoFailureException {

        if (validationSkip) {
            getLog().info("Skipping annotation validation.");
            return;
        }

        getLog().info(String.format("Executing plugin for project '%s'", project.getName()));
        initialiseClassLoader();

        final List<ValidationResult> globalValidationResults = newArrayList();
        for (String annotationClass : annotations) {
            globalValidationResults.addAll(processAnnotation(classLoader, annotationClass));
        }

        final long failedValidationCount = globalValidationResults.stream().filter(vr -> !vr.isValidationPassed()).count();
        if (failedValidationCount > 0) {
            reportError("Found " + failedValidationCount + " validation errors");
        } else {
            getLog().info("No validation errors found.");
        }

    }

    private List<ValidationResult> processAnnotation(final URLClassLoader classLoader, final String annotationClass) throws MojoFailureException {

        final List<ValidationResult> annotationValidationResults = newArrayList();
        final ClassInfoList annotationClasses = new ClassGraph().enableAnnotationInfo().scan().getClassesWithAnnotation(annotationClass);
        final AnnotationValidator annotationValidator = getValidator(classLoader, annotationClass);
        final int numberOfClassesImplementingAnnotation = annotationClasses.getNames().size();
        getLog().info("Found " + numberOfClassesImplementingAnnotation + " classes for annotation '" + annotationClass + "'");
        for (String nameOfAnnotation : annotationClasses.getNames()) {
            try {
                annotationValidationResults.add(annotationValidator.validate(forName(nameOfAnnotation, false, classLoader), validatorProperties));
            } catch (final ClassNotFoundException e) {
                reportError("Cannot find class - " + e.getMessage());
            }
        }

        if (generateReport && !annotationValidationResults.isEmpty()) {
            new AnnotationValidationReport().generateReport(new ReportConfig(annotationClass, project.getBasedir().getAbsolutePath(), annotationValidationResults));
        }
        return annotationValidationResults;
    }

    private void initialiseClassLoader() throws MojoFailureException {
        try {
            if (null == classLoader) {
                final String targetClassesDirectory = project.getBuild().getOutputDirectory();
                getLog().info("Consuming classes from directory : " + targetClassesDirectory);

                classLoader = new URLClassLoader(new URL[]{new File(targetClassesDirectory).toURI().toURL()}, currentThread().getContextClassLoader());
            }
            currentThread().setContextClassLoader(classLoader);
        } catch (final MalformedURLException e) {
            reportError(e.getMessage());
        }
    }

    private void reportError(final String errorMessage) throws MojoFailureException {
        getLog().error(errorMessage);
        if (failBuildOnError) {
            throw new MojoFailureException(errorMessage);
        }
    }

}
