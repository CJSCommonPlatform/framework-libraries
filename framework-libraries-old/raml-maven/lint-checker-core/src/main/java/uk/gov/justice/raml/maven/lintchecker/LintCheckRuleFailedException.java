package uk.gov.justice.raml.maven.lintchecker;

public class LintCheckRuleFailedException extends LintCheckerException {

    public LintCheckRuleFailedException(final String message) {
        super(message);
    }

    public LintCheckRuleFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
