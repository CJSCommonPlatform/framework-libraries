package uk.gov.justice.raml.maven.lintchecker;

import org.junit.jupiter.api.Test;

public class LintCheckPluginExceptionTest {

    @Test
    public void shouldCreateException() {
        final LintCheckPluginException exception = new LintCheckPluginException("test");
        new LintCheckPluginException("test", exception);
    }

}