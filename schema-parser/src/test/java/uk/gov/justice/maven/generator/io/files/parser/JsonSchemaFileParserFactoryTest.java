package uk.gov.justice.maven.generator.io.files.parser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.json.JsonObject;

import org.junit.Test;

public class JsonSchemaFileParserFactoryTest {

    @Test
    public void shouldCreateJsonSchemaFileParser() throws Exception {

        final FileParser<JsonObject> jsonObjectFileParser = new JsonSchemaFileParserFactory().create();

        assertThat(jsonObjectFileParser, is(instanceOf(JsonSchemaFileParser.class)));
    }

}