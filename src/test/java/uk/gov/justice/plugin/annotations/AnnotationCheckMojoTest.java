package uk.gov.justice.plugin.annotations;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.After;
import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.maven.test.utils.BetterAbstractMojoTestCase;
import uk.gov.justice.plugin.domain.ClassWithInvalidEventAnnotation;
import uk.gov.justice.plugin.domain.ClassWithValidEventAnnotation;
import uk.gov.justice.plugin.domain.TestAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Joiner.on;
import static org.apache.commons.io.FileUtils.readLines;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * The tests below rely on a dummy java project with compiled source code which can be found in the following location
 * /src/test/resources/annotation-plugin
 * <p>
 * The project has been compiled and the target directory with compiled classes preserved for running these tests
 */
public class AnnotationCheckMojoTest extends BetterAbstractMojoTestCase {

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
        final File pom = new File(PROJECT_BASE_DIR + "/pom-generate-report.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        mojo.execute();

        // assert report generation and content
        verifyReport();
    }

    public void testShouldScanClassesWithAnnotationsAndGenerateReportWhilstFailingBuild() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-generate-report-fail-build.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        try {
            mojo.execute();
            fail("Should have thrown an exception to fail build");
        } catch (MojoFailureException e) {

            // assert report generation and content
            verifyReport();
        }

    }

    public void testShouldSkipValidation() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-skip-validation.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        mojo.execute();

        verifyReportNotGenerated();
    }

    public void testMultipleReports() throws Exception {
        final File pom = new File(PROJECT_BASE_DIR + "/pom-generate-multiple-reports.xml");
        final AnnotationCheckMojo mojo = (AnnotationCheckMojo) lookupConfiguredMojo(pom, MAVEN_GOAL_CHECK_ANNOTATIONS);

        mojo.execute();

        verifyReportGenerated(Event.class);
        verifyReportGenerated(TestAnnotation.class);
    }

    private void verifyReport() throws IOException {
        final List<String> reportLines = readLines(new File(getQualifiedPathForReport(Event.class)));
        assertThat(reportLines, hasSize(2));
        assertThat(reportLines, hasItem(getCsvLine(ClassWithValidEventAnnotation.class.getName(), "structure.events.valid", true)));
        assertThat(reportLines, hasItem(getCsvLine(ClassWithInvalidEventAnnotation.class.getName(), "structure.invalid", false)));
    }

    private void verifyReportNotGenerated() {
        final File file = new File(getQualifiedPathForReport(Event.class));
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