package uk.gov.justice.raml.maven.lintchecker.rules;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.maven.test.utils.BetterAbstractMojoTestCase;
import uk.gov.justice.raml.maven.lintchecker.LintCheckerException;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

public class LintCheckMojoTest extends BetterAbstractMojoTestCase {


    public void testFindAllMojosUnderTheSourceDirectory() throws Exception {

        final File pom = getTestFile("src/test/resources/lint-check/execute/pom.xml");
        final LintCheckMojo mojo = (LintCheckMojo) lookupConfiguredMojo(pom, "raml-lint-check");
        mojo.execute();
        assertThat(true, is(new SucceedingLintCheckRule().tripped));
    }

    public void testRunAllRulesAndFailIfTheRuleFails() throws Exception {

        final File pom = getTestFile("src/test/resources/lint-check/exception/pom.xml");
        final LintCheckMojo mojo = (LintCheckMojo) lookupConfiguredMojo(pom, "raml-lint-check");

        try {
            mojo.execute();
            fail();
        } catch (final MojoExecutionException expected) {
            assertThat(expected.getMessage(), is("Lint checker rule failed for rule FailingLintCheckRule"));
            assertThat(expected.getCause(), is(instanceOf(LintCheckerException.class)));
        }
    }
}
