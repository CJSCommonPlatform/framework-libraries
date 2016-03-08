package uk.gov.justice.raml.jaxrs.maven;

import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;
import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Service for calling a generator on some raml
 */
public class GenerateGoalProcessor {

    private final GeneratorFactory generatorFactory;

    public GenerateGoalProcessor(final GeneratorFactory generatorFactory) {
        this.generatorFactory = generatorFactory;
    }

    public void generate(final GenerateGoalConfig config) throws IOException {

        final String[] includes = {"**/*.raml"};
        final String[] excludes = {};
        final GeneratorConfig generatorConfig = new GeneratorConfig(config.getSourceDirectory(), config.getOutputDirectory(), config.getBasePackageName());

        final Generator generator = generatorFactory.create(config.getGeneratorName());

        Collection<Path> paths = new FileTreeScanner().find(config.getSourceDirectory(), includes, excludes);

        new RamlFileParser()
                .parse(config.getSourceDirectory(), paths)
                .stream()
                .forEach(raml -> generator.run(raml, generatorConfig));
    }

}
