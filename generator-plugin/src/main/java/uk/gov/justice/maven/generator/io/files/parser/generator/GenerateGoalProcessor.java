package uk.gov.justice.maven.generator.io.files.parser.generator;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;
import uk.gov.justice.maven.generator.io.files.parser.io.FileTreeScannerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Service for calling a generator on RAML files.
 */
public class GenerateGoalProcessor {

    private final MojoGeneratorFactory mojoGeneratorFactory;
    private final FileParser parser;

    private final FileTreeScannerFactory scannerFactory;

    public GenerateGoalProcessor(final MojoGeneratorFactory mojoGeneratorFactory,
                                 final FileTreeScannerFactory scannerFactory,
                                 final FileParser parser) {
        this.mojoGeneratorFactory = mojoGeneratorFactory;
        this.scannerFactory = scannerFactory;
        this.parser = parser;
    }

    @SuppressWarnings("unchecked")
    public void generate(final GenerateGoalConfig config) throws IOException {

        final String[] includes = config.getIncludes().toArray(new String[config.getIncludes().size()]);
        final String[] excludes = config.getExcludes().toArray(new String[config.getExcludes().size()]);

        final GeneratorConfig generatorConfig = new GeneratorConfig(config.getSourceDirectory(),
                config.getOutputDirectory(), config.getBasePackageName(), config.getProperties(), config.getSourcePaths());

        final Collection<Path> paths = scannerFactory.create().find(config.getSourceDirectory(), includes, excludes);
        parser
                .parse(config.getSourceDirectory(), paths)
                .forEach(file -> mojoGeneratorFactory.instanceOf(config.getGeneratorName()).run(file, generatorConfig));
    }
}
