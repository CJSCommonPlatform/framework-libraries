package uk.gov.justice.maven.generator.io.files.parser.json.test;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorFactory;

import javax.json.JsonObject;

public class JsonTitleAppendingGeneratorFactory implements GeneratorFactory<JsonObject> {

    @Override
    public Generator<JsonObject> create() {
        return new JsonTitleAppendingGenerator(new FilePathProvider());
    }
}
