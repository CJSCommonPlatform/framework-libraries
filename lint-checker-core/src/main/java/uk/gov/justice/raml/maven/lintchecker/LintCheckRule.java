package uk.gov.justice.raml.maven.lintchecker;

import org.raml.model.Raml;

/**
 * Interface to be implemented by any rules executed by the lint checker.
 */
public interface LintCheckRule {

    /**
     * Validate that the Raml and project structure
     *
     * @param raml The raml file to be validated
     * @param lintCheckConfiguration The global configuration that a rule may need
     * @throws LintCheckerException if the validation fails
     */
    void execute(final Raml raml, final LintCheckConfiguration lintCheckConfiguration) throws LintCheckerException;

}
