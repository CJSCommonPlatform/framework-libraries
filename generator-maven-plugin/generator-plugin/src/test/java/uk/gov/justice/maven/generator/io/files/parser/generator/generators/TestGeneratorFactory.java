package uk.gov.justice.maven.generator.io.files.parser.generator.generators;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorFactory;

public class TestGeneratorFactory implements GeneratorFactory {

    @Override
    public Generator create() {
        return new TestGenerator();
    }
}
