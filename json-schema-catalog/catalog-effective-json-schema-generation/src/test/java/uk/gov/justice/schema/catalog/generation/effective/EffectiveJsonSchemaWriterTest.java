package uk.gov.justice.schema.catalog.generation.effective;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EffectiveJsonSchemaWriterTest {

    @TempDir
    Path tempDir;

    @InjectMocks
    private EffectiveJsonSchemaWriter effectiveJsonSchemaWriter;

    @Test
    public void shouldWriteEffectiveJsonSchemaToFile() throws Exception {
        final JSONObject schema = new JSONObject("""
                { "id": "http://example.com/person.json", "type": "object" }
                """);

        final Path outputPath = tempDir.resolve("context/person.json");

        effectiveJsonSchemaWriter.write(schema, outputPath);

        assertThat(Files.exists(outputPath), is(true));
        final JSONObject written = new JSONObject(Files.readString(outputPath));
        assertThat(written.getString("id"), is("http://example.com/person.json"));
        assertThat(written.getString("type"), is("object"));
    }

    @Test
    public void shouldCreateIntermediateParentDirectories() throws Exception {
        final JSONObject schema = new JSONObject("{ \"type\": \"object\" }");
        final Path outputPath = tempDir.resolve("deep/nested/path/schema.json");

        effectiveJsonSchemaWriter.write(schema, outputPath);

        assertThat(Files.exists(outputPath), is(true));
    }

    @Test
    public void shouldWritePrettyPrintedJson() throws Exception {
        final JSONObject schema = new JSONObject("""
                { "id": "http://example.com/test.json", "type": "object" }
                """);

        final Path outputPath = tempDir.resolve("test.json");

        effectiveJsonSchemaWriter.write(schema, outputPath);

        final String content = Files.readString(outputPath);
        assertThat(content.contains("\n"), is(true));
    }
}
