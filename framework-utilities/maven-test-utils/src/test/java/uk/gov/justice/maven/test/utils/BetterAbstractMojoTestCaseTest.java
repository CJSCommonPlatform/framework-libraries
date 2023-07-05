package uk.gov.justice.maven.test.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.junit.jupiter.api.Test;

public class BetterAbstractMojoTestCaseTest {

    private BetterAbstractMojoTestCase mojoTestCase = new BetterAbstractMojoTestCase() {
        @Override
        protected Mojo lookupConfiguredMojo(final File pom, final String goal) throws Exception {
            return super.lookupConfiguredMojo(pom, goal);
        }
    };

    /**
     * Adding this test for coveralls report generation
     */

    @Test
    public void emptyAssertion() {
        assertTrue(true);
    }
}
