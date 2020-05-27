package uk.gov.justice.maven.generator.io.files.parser.generator;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import org.raml.model.Raml;

/**
 * Generator for testing - the RAML and configuration are captured by the DummyGeneratorCaptor
 * singleton class to be further used in unit tests.
 */
public class DummyGenerator implements Generator<Raml> {

    @Override
    public void run(Raml raml, GeneratorConfig generatorConfig) {
        DummyGeneratorCaptor.getInstance().capture(raml, generatorConfig);
    }
}
