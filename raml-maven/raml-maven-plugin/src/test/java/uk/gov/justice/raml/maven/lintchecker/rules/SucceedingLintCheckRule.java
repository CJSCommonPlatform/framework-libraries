package uk.gov.justice.raml.maven.lintchecker.rules;

import uk.gov.justice.raml.maven.lintchecker.LintCheckConfiguration;
import uk.gov.justice.raml.maven.lintchecker.LintCheckerException;

import org.raml.model.Raml;

public class SucceedingLintCheckRule implements LintCheckRule {


    public static boolean tripped;

    @Override
    public void execute(final Raml raml, final LintCheckConfiguration lintCheckConfiguration) throws LintCheckerException {
        tripped = true;
    }
}
