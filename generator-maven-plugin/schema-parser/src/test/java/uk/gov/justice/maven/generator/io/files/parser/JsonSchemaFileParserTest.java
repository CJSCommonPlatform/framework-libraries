package uk.gov.justice.maven.generator.io.files.parser;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.junit.jupiter.api.Test;

public class JsonSchemaFileParserTest {

    final Path baseDir = get("src/test/resources/event/");
    final JsonSchemaFileParser parser = new JsonSchemaFileParser();

    @Test
    public void shouldParsePathsToJson() throws URISyntaxException {

        final List<Path> paths = asList(
                get("test-schema1.json"),
                get("test-schema2.json"));
        final Collection<JsonObject> schemas = parser.parse(
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
        final List<Path> paths = List.of(get("no-schema1.json"));

        final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> parser.parse(baseDir, paths));
        assertThat(runtimeException.getCause(), is(instanceOf(FileNotFoundException.class)));
    }
}