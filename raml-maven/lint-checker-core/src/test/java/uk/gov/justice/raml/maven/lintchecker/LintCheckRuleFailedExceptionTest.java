package uk.gov.justice.raml.maven.lintchecker;

import org.junit.Test;

public class LintCheckRuleFailedExceptionTest {


    @Test
    public void shouldCreateException() {
        final LintCheckerException exception = new LintCheckRuleFailedException("test");
        new LintCheckRuleFailedException("test", exception);
    }

}