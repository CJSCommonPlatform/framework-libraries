package uk.gov.justice.raml.maven.lintchecker;

/**
 * Exception that is thrown if any of the {@link LintCheckRule}s fail.
 */
public class LintCheckerException extends Exception {

    public LintCheckerException(final String message) {
        super(message);
    }
}
