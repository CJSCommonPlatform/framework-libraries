package uk.gov.justice.plugin.annotation;

import static com.google.common.base.Joiner.on;
import static org.apache.commons.io.FileUtils.readLines;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.maven.test.utils.BetterAbstractMojoTestCase;
import uk.gov.justice.plugin.domain.AnotherTestAnnotation;
import uk.gov.justice.plugin.domain.ClassWithInvalidEventAnnotation;
import uk.gov.justice.plugin.domain.ClassWithValidEventAnnotation;
import uk.gov.justice.plugin.domain.TestAnnotation;
import uk.gov.justice.plugin.domain.TestAnnotationWithoutValidator;
import uk.gov.justice.plugin.exception.ValidatorNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.After;

/**
 * The tests below rely on a dummy java project with compiled source code which can be found in the
 * following location /src/test/resources/annotation-plugin
 * <p>
 * The project has been compiled and the target directory with compiled classes preserved for
 * running these tests
 */
public class AnnotationCheckMojoTest extends BetterAbstractMojoTestCase {

    /**
     * NOTE: Any changes to the plugin will require an update to the version of plugin under the
     * test POMs present under src/test/resources/annotation-plugin folder.  The plugin version
     * should match the project's POM version
     */

    private static final String PLUGIN_VALIDATION_RESULT_PREFIX = "annotation-plugin-validation-result-";
    private static final String PROJECT_BASE_DIR = "src/test/resources/annotation-plugin";
    private static final String REPORT_FILE_EXTENSION = ".csv";
    private static final String MAVEN_GOAL_CHECK_ANNOTATIONS = "check-annotations";

    @After
    public void tearDown() {
        for (File f : new File(PROJECT_BASE_DIR).listFiles()) {
            if (f.getName().startsWith(PLUGIN_VALIDATION_RESULT_PREFIX)) {
                f.delete();
            }
        }
    }

    public void testShouldScanClassesWithAnnotationsAndGenerateReportWithoutFailingBuild() throws Exception {
        this.getClass().getPackage().getImplementationVersion();

        final File pom = new File(PROJECT_BASE_DIR + "/pom-generate-report.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        mojo.execute();

        // assert report generation and content
        verifyReport("");
    }

    public void testShouldScanClassesWithAnnotationsAndGenerateReportWhilstFailingBuild() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-generate-report-fail-build.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        try {
            mojo.execute();
            fail("Should have thrown an exception to fail build");
        } catch (MojoFailureException e) {

            // assert report generation and content
            verifyReport("");
        }

    }

    public void testShouldSkipValidation() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-skip-validation.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        mojo.execute();

        verifyReportNotGenerated(TestAnnotation.class);
    }

    public void testMultipleReports() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-generate-multiple-reports.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        mojo.execute();

        verifyReportGenerated(TestAnnotation.class);

        // as no validation results found - report will not be generated
        verifyReportNotGenerated(AnotherTestAnnotation.class);
    }

    public void testShouldScanClassesWithAnnotationsAndGenerateReportUsingAdditionalProperty() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-generate-report-with-additional-validator-properties.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        mojo.execute();

        // assert report generation and content
        verifyReport("dummy message");
    }


    public void testShouldThrowExceptionAsUsedAnnotationHasNoValidator() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-fail-annotation-with-no-validator.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        try {
            mojo.execute();
            fail("Exception should be thrown");
        } catch (ValidatorNotFoundException e) {
            assertThat(e.getMessage(), containsString(TestAnnotationWithoutValidator.class.getName()));
        }

    }

    private void verifyReport(final String validationFailureMessageText) throws IOException {
        final List<String> reportLines = readLines(new File(getQualifiedPathForReport(TestAnnotation.class)));
        assertThat(reportLines, hasSize(2));
        assertThat(reportLines, hasItem(getCsvLine(ClassWithValidEventAnnotation.class.getName(), "events.valid", true)));
        assertThat(reportLines, hasItem(getCsvLine(ClassWithInvalidEventAnnotation.class.getName(), validationFailureMessageText, false)));
    }

    private void verifyReportNotGenerated(final Class clazz) {
        final File file = new File(getQualifiedPathForReport(clazz));
        assertThat(file.exists(), is(false));
    }

    private void verifyReportGenerated(final Class clazz) {
        final File file = new File(getQualifiedPathForReport(clazz));
        assertThat(file.exists(), is(true));
    }

    private String getCsvLine(final String className, final String annotationAttributeValue, final boolean validationResult) {
        return on(",").join(new Object[]{className, annotationAttributeValue, validationResult});
    }


    private String getQualifiedPathForReport(final Class clazz) {
        return PROJECT_BASE_DIR + File.separator + PLUGIN_VALIDATION_RESULT_PREFIX + clazz.getName() + REPORT_FILE_EXTENSION;
    }


}