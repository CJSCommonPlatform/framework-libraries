package uk.gov.justice.raml.maven.validator;

import org.raml.parser.loader.FileResourceLoader;
import org.raml.parser.rule.ValidationResult;
import org.raml.parser.visitor.RamlValidationService;
import uk.gov.justice.raml.io.FileTreeScannerFactory;
import uk.gov.justice.raml.maven.common.BasicGoalConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RamlSyntaxValidator {

    private final FileTreeScannerFactory scannerFactory;

    public RamlSyntaxValidator(final FileTreeScannerFactory scannerFactory) {

        this.scannerFactory = scannerFactory;

    }

    public Map<Path, ValidationResult> validateRamls(final BasicGoalConfig config) throws IOException {

        final String[] includes = config.getIncludes().toArray(new String[config.getIncludes().size()]);
        final String[] excludes = config.getExcludes().toArray(new String[config.getExcludes().size()]);

        final Collection<Path> paths = scannerFactory.create().find(config.getSourceDirectory(), includes, excludes);

        Map<Path, ValidationResult> results = new HashMap<>();
        for (Path path : paths) {
            List<ValidationResult> tmpResults = validationResults(path, config);
            for (ValidationResult result : tmpResults) {
                results.put(path, result);
            }
        }

        return results;
    }

    private List<ValidationResult> validationResults(final Path path, final BasicGoalConfig config) {

        return RamlValidationService.createDefault(
                new FileResourceLoader(config.getSourceDirectory().toFile())).validate(path.toFile().getName());

    }

}
