package uk.gov.justice.raml.jaxrs.maven;

import uk.gov.justice.raml.core.GeneratorConfig;
import uk.gov.justice.raml.io.FileTreeScannerFactory;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Service for calling a generator on RAML files.
 */
public class GenerateGoalProcessor {

    private final GeneratorFactory generatorFactory;

    private final FileTreeScannerFactory scannerFactory;

    public GenerateGoalProcessor(final GeneratorFactory generatorFactory, final FileTreeScannerFactory scannerFactory) {
        this.generatorFactory = generatorFactory;
        this.scannerFactory = scannerFactory;
    }

    public void generate(final GenerateGoalConfig config) throws IOException {

        final String[] includes = config.getIncludes().toArray(new String[config.getIncludes().size()]);
        final String[] excludes = config.getExcludes().toArray(new String[config.getExcludes().size()]);

        final GeneratorConfig generatorConfig = new GeneratorConfig(config.getSourceDirectory(),
                config.getOutputDirectory(), config.getBasePackageName(), config.getProperties());

        final Collection<Path> paths = scannerFactory.create().find(config.getSourceDirectory(), includes, excludes);

        new RamlFileParser()
                .ramlOf(config.getSourceDirectory(), paths)
                .stream()
                .forEach(raml -> generatorFactory.instanceOf(config.getGeneratorName()).run(raml, generatorConfig));
    }
}
