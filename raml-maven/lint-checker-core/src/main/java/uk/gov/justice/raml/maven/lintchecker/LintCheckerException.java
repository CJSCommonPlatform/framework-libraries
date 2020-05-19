package uk.gov.justice.raml.maven.lintchecker;

import uk.gov.justice.raml.maven.lintchecker.rules.LintCheckRule;

/**
 * Exception that is thrown if any of the {@link LintCheckRule}s fail.
 */
public abstract class LintCheckerException extends Exception {

    public LintCheckerException(final String message) {
        super(message);
    }

    public LintCheckerException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
