package uk.gov.justice.raml.io.files.parser;

import org.junit.Test;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    @Test
    public void shouldThrowFileNotFoundException() {
        final Path baseDir = get("");
        final List<Path> paths = asList(get("test-schema1.json"));
        Collection<JsonObject> result = parser.parse(baseDir, paths);
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(nullValue()));
    }
}