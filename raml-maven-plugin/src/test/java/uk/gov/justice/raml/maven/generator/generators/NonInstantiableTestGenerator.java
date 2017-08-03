package uk.gov.justice.raml.maven.generator.generators;

import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;

import org.raml.model.Raml;

public class NonInstantiableTestGenerator implements Generator<Raml> {

    private NonInstantiableTestGenerator() {}

    @Override
    public void run(Raml source, GeneratorConfig generatorConfig) {

    }
}
