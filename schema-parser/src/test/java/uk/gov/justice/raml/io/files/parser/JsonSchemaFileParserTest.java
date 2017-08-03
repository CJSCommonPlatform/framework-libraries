package uk.gov.justice.raml.io.files.parser;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.junit.Test;

public class JsonSchemaFileParserTest {

    final JsonSchemaFileParser parser = new JsonSchemaFileParser();

    @Test
    public void shouldParsePathsToJson() throws URISyntaxException {

        final Path baseDir = get("src/test/resources/event/");

        final List<Path> paths = asList(
                get("test-schema1.json"),
                get("test-schema2.json"));
        Collection<JsonObject> schemas = parser.parse(
                baseDir,
                paths);

        assertThat(schemas, hasSize(2));
        assertThat(schemas, allOf(
                hasItem(
                        allOf(
                                hasEntry("additionalProperties", JsonValue.FALSE)
                        )
                ),
                hasItem(
                        allOf(
                                hasEntry("additionalProperties", JsonValue.FALSE)
                        )
                )
                )
        );
    }
}