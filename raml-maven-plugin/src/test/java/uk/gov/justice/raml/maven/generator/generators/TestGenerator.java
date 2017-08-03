package uk.gov.justice.raml.maven.generator.generators;

import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;

import org.raml.model.Raml;

public class TestGenerator implements Generator <Raml>{

    @Override
    public void run(final Raml raml, final GeneratorConfig generatorConfig) {
    }
}
