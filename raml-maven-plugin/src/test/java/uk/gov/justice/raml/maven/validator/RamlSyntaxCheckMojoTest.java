package uk.gov.justice.raml.maven.validator;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.raml.maven.common.BasicGoalConfig;
import uk.gov.justice.raml.maven.generator.BetterAbstractMojoTestCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.raml.parser.rule.ValidationResult;

public class RamlSyntaxCheckMojoTest extends BetterAbstractMojoTestCase {

    public void testShouldFindRamlsAndThrowMojoFailureException() throws Exception {

        final File pom = getTestFile("src/test/resources/validator/bad-schema/pom.xml");

        final RamlSyntaxCheckMojo mojo = (RamlSyntaxCheckMojo) lookupConfiguredMojo(pom, "check-syntax");

        try {
            mojo.execute();
            fail("Raml and schema validation should have found errors");
        } catch (MojoFailureException e) {
            //test passed
        }
    }

    @SuppressWarnings("unchecked")
    public void testShouldFindMultipleRamlsAndThrowMojoFailureException() throws Exception {

        final File pom = getTestFile("src/test/resources/validator/multiple-ramls/pom.xml");

        final RamlSyntaxCheckMojo mojo = (RamlSyntaxCheckMojo) lookupConfiguredMojo(pom, "check-syntax");

        try {
            mojo.execute();
            fail("Raml and schema validation should have found errors");
        } catch (MojoFailureException e) {
            Map<Path, ValidationResult> results = (Map<Path, ValidationResult>) e.getSource();
            assertThat(results.keySet(), hasItems(Paths.get("test1.raml"), Paths.get("test2.raml")));
            assertThat(results.keySet(), hasSize(2));
            ValidationResult validationResult1 = results.get(Paths.get("test1.raml"));
            assertThat(validationResult1.getMessage(), containsString("invalid JSON schema (json/schema/add-recipe.json)"));
            assertThat(validationResult1.getMessage(), containsString("keyword: \"minItems\""));

            ValidationResult validationResult2 = results.get(Paths.get("test2.raml"));
            assertThat(validationResult2.getMessage(), containsString("could not find expected ':'"));

        }
    }

    public void testShouldValidateOkIfSchemaHasNoErrors() throws Exception {

        final File pom = getTestFile("src/test/resources/validator/good-raml/pom.xml");

        final RamlSyntaxCheckMojo mojo = (RamlSyntaxCheckMojo) lookupConfiguredMojo(pom, "check-syntax");

        mojo.execute();

    }


    public void testShouldWrapIOExceptionInMojoFailureException() throws Exception {
        File pom = getTestFile("src/test/resources/validator/good-raml/pom.xml");

        RamlSyntaxCheckMojo mojo = (RamlSyntaxCheckMojo) lookupConfiguredMojo(pom, "check-syntax");

        RamlSyntaxValidator ramlSyntaxValidator = mock(RamlSyntaxValidator.class);
        IOException ioExceptionThrown = new IOException();
        when(ramlSyntaxValidator.validateRamls(any(BasicGoalConfig.class))).thenThrow(ioExceptionThrown);
        mojo.ramlSyntaxValidator = ramlSyntaxValidator;
        try {
            mojo.execute();
            fail("Raml and schema validation should have found errors");
        } catch (MojoExecutionException e) {
            assertThat(e.getCause(), equalTo(ioExceptionThrown));
        }
    }

    public void testShouldPassIfNoRamlFound() throws Exception {

        File pom = getTestFile("src/test/resources/validator/no-raml/pom.xml");

        RamlSyntaxCheckMojo mojo = (RamlSyntaxCheckMojo) lookupConfiguredMojo(pom, "check-syntax");

        mojo.execute();
    }
}

