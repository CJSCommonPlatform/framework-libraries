package uk.gov.justice.raml.maven.lintchecker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.raml.maven.lintchecker.rules.FailingLintCheckRule;
import uk.gov.justice.raml.maven.lintchecker.rules.SucceedingLintCheckRule;
import uk.gov.justice.raml.maven.test.utils.BetterAbstractMojoTestCase;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

public class LintCheckMojoTest extends BetterAbstractMojoTestCase {


    public void testFindAllMojosUnderTheSourceDirectory() throws Exception {

        final File pom = getTestFile("src/test/resources/lint-check/pom.xml");
        final LintCheckMojo mojo = (LintCheckMojo) lookupConfiguredMojo(pom, "lint-check");

        assertThat(mojo.rules.size(), is(2));
        assertThat(mojo.rules.get(0), is(instanceOf(SucceedingLintCheckRule.class)));
        assertThat(mojo.rules.get(1), is(instanceOf(FailingLintCheckRule.class)));
    }

    public void testRunAllRulesAndFailIfTheRuleFails() throws Exception {

        final File pom = getTestFile("src/test/resources/lint-check/pom.xml");
        final LintCheckMojo mojo = (LintCheckMojo) lookupConfiguredMojo(pom, "lint-check");

        try {
            mojo.execute();
            fail();
        } catch (final MojoExecutionException expected) {
            assertThat(expected.getMessage(), is("Lint checker rule failed for rule FailingLintCheckRule"));
            assertThat(expected.getCause(), is(instanceOf(LintCheckerException.class)));
        }
    }
}
