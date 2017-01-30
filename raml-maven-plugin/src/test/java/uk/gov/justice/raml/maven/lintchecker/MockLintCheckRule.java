package uk.gov.justice.raml.maven.lintchecker;

import org.raml.model.Raml;

public class MockLintCheckRule implements LintCheckRule{

    @Override
    public void execute(Raml raml) throws LintCheckerException {
        throw new LintCheckerException("LintCheckRule applied");
    }
}
