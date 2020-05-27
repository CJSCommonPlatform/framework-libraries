package uk.gov.justice.raml.maven.lintchecker;

public class LintCheckPluginException extends LintCheckerException {

    public LintCheckPluginException(final String message) {
        super(message);
    }

    public LintCheckPluginException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
