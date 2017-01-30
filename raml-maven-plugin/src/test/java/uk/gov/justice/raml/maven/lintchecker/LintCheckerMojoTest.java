package uk.gov.justice.raml.maven.lintchecker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.raml.maven.generator.BetterAbstractMojoTestCase;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

public class LintCheckerMojoTest extends BetterAbstractMojoTestCase {

    public void testShouldFindLintCheckMojo() throws Exception {

        File pom = getTestFile("src/test/resources/lint-check/pom.xml");

        LintCheckerMojo mojo = (LintCheckerMojo) lookupConfiguredMojo(pom, "lint-check");

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException expected) {
            assertThat(expected.getMessage(), is("Lint checker rule MockLintCheckRule has failed"));
            assertThat(expected.getCause(), is(instanceOf(LintCheckerException.class)));
        }
    }
}