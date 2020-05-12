package uk.gov.justice.maven.generator.io.files.parser;

import javax.json.JsonObject;

public class JsonSchemaFileParserFactory implements FileParserFactory<JsonObject> {

    @Override
    public FileParser<JsonObject> create() {
        return new JsonSchemaFileParser();
    }
}
