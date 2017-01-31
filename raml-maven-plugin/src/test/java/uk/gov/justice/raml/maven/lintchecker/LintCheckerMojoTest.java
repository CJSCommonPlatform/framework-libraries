package uk.gov.justice.raml.maven.lintchecker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.raml.maven.generator.BetterAbstractMojoTestCase;
import uk.gov.justice.raml.maven.lintchecker.rules.FailingLintCheckRule;
import uk.gov.justice.raml.maven.lintchecker.rules.SucceedingLintCheckRule;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class LintCheckerMojoTest extends BetterAbstractMojoTestCase {


    public void testFindAllMojosUnderTheSourceDirectory() throws Exception {

        final File pom = getTestFile("src/test/resources/lint-check/pom.xml");
        final LintCheckerMojo mojo = (LintCheckerMojo) lookupConfiguredMojo(pom, "lint-check");

        assertThat(mojo.rules.length, is(2));
        assertThat(mojo.rules[0], is(instanceOf(SucceedingLintCheckRule.class)));
        assertThat(mojo.rules[1], is(instanceOf(FailingLintCheckRule.class)));
    }

    public void testRunAllRulesAndFailIfTheRuleFails() throws Exception {

        final File pom = getTestFile("src/test/resources/lint-check/pom.xml");
        final LintCheckerMojo mojo = (LintCheckerMojo) lookupConfiguredMojo(pom, "lint-check");

        try {
            mojo.execute();
            fail();
        } catch (final MojoExecutionException expected) {
            assertThat(expected.getMessage(), is("Lint checker rule failed for rule FailingLintCheckRule"));
            assertThat(expected.getCause(), is(instanceOf(LintCheckerException.class)));
        }
    }
}
