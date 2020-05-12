package uk.gov.justice.maven.generator.io.files.parser.generator.generators;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import org.raml.model.Raml;

public class NonInstantiableTestGenerator implements Generator<Raml> {

    private NonInstantiableTestGenerator() {}

    @Override
    public void run(final Raml source, final GeneratorConfig generatorConfig) {

    }
}
