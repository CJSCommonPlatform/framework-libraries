package uk.gov.justice.raml.maven.lintchecker;

import org.raml.model.Raml;

/**
 * Interface to be implemented by any rules executed by the lint checker.
 */
public interface LintCheckRule {

    void execute(final Raml raml) throws LintCheckerException;

}
