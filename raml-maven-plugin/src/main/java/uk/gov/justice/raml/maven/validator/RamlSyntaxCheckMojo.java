package uk.gov.justice.raml.maven.validator;

import uk.gov.justice.raml.io.FileTreeScannerFactory;
import uk.gov.justice.raml.maven.common.BasicGoalConfig;
import uk.gov.justice.raml.maven.common.BasicMojo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.raml.parser.rule.ValidationResult;

@Mojo(name = "check-syntax")
public class RamlSyntaxCheckMojo extends BasicMojo {

    RamlSyntaxValidator ramlSyntaxValidator = new RamlSyntaxValidator(new FileTreeScannerFactory());

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        configureDefaultFileIncludes();

        report(ramlValidationResults());
    }

    private void report(final Map<Path, ValidationResult> results) throws MojoFailureException {
        if (results.isEmpty()) {
            getLog().info("Raml and Json Schema Validation Complete");
        } else {
            String validationResultsPrint = printResults(results, sourceDirectory);

            getLog().debug(validationResultsPrint);

            throw new MojoFailureException(results, "Raml & Json schema validation has failed", validationResultsPrint);
        }
    }

    private Map<Path, ValidationResult> ramlValidationResults() throws MojoExecutionException {
        try {
            return ramlSyntaxValidator.validateRamls(configuration());
        } catch (IOException e) {
            throw new MojoExecutionException("Files not loaded", e);
        }
    }

    private String printResults(final Map<Path, ValidationResult> results, final File sourceDirectory) {

        final StringBuilder sb = new StringBuilder();

        sb.append("\nThere are ");
        sb.append(results.size());
        sb.append(" validation errors: \n");

        results.entrySet().stream()
                .forEach(resultEntry -> {
                    sb.append("\nSyntax check has failed for ");
                    sb.append(sourceDirectory);
                    sb.append(resultEntry.getKey());
                    sb.append("\nError Level : ");
                    sb.append(resultEntry.getValue().getLevel());
                    sb.append(" Line : ");
                    sb.append(resultEntry.getValue().getLine());
                    sb.append(" Column : ");
                    sb.append(resultEntry.getValue().getStartColumn());
                    sb.append("\n");
                    sb.append(resultEntry.getValue().getMessage());
                    sb.append("\n");
                });
        return sb.toString();

    }

    private BasicGoalConfig configuration() {

        return new BasicGoalConfig(sourceDirectory.toPath(),
                includes,
                excludes);

    }

}
