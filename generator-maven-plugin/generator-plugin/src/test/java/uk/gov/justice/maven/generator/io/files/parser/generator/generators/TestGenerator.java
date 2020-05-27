package uk.gov.justice.maven.generator.io.files.parser.generator.generators;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import org.raml.model.Raml;

public class TestGenerator implements Generator <Raml>{

    @Override
    public void run(final Raml raml, final GeneratorConfig generatorConfig) {
    }
}
