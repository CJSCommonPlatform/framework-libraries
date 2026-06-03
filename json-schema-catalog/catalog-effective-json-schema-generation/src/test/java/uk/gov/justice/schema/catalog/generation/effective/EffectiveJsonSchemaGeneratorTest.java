package uk.gov.justice.schema.catalog.generation.effective;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EffectiveJsonSchemaGeneratorTest {

    @TempDir
    Path tempDir;

    @Mock
    private JsonSchemaInliner jsonSchemaInliner;

    @Mock
    private EffectiveJsonSchemaWriter effectiveJsonSchemaWriter;

    @InjectMocks
    private EffectiveJsonSchemaGenerator effectiveJsonSchemaGenerator;

    @Test
    public void shouldCallInlinerAndWriterForEachSchemaFile() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);
        final Path schemaFile = sourceDir.resolve("person.json");
        Files.writeString(schemaFile, "{ \"id\": \"http://example.com/person.json\", \"type\": \"object\" }");

        final Path outputDir = tempDir.resolve("output");
        final Map<String, String> allSchemas = Map.of("http://example.com/person.json", "{}");
        final JSONObject inlinedSchema = new JSONObject("{ \"type\": \"object\" }");

        when(jsonSchemaInliner.inline(any(JSONObject.class), eq(allSchemas))).thenReturn(inlinedSchema);

        effectiveJsonSchemaGenerator.generate(List.of(schemaFile), sourceDir, allSchemas, outputDir);

        final ArgumentCaptor<JSONObject> schemaCaptor = ArgumentCaptor.forClass(JSONObject.class);
        final ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
        verify(effectiveJsonSchemaWriter).write(schemaCaptor.capture(), pathCaptor.capture());

        assertThat(schemaCaptor.getValue().getString("type"), is("object"));
        assertThat(pathCaptor.getValue(), is(outputDir.resolve("person.json")));
    }

    @Test
    public void shouldPreserveRelativeDirectoryStructureInOutput() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        final Path contextDir = sourceDir.resolve("context");
        Files.createDirectories(contextDir);
        final Path schemaFile = contextDir.resolve("person.json");
        Files.writeString(schemaFile, "{ \"id\": \"http://example.com/person.json\" }");

        final Path outputDir = tempDir.resolve("output");
        final JSONObject inlinedSchema = new JSONObject("{}");

        when(jsonSchemaInliner.inline(any(JSONObject.class), any())).thenReturn(inlinedSchema);

        effectiveJsonSchemaGenerator.generate(List.of(schemaFile), sourceDir, Map.of(), outputDir);

        final ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
        verify(effectiveJsonSchemaWriter).write(any(), pathCaptor.capture());

        assertThat(pathCaptor.getValue(), is(outputDir.resolve("context/person.json")));
    }

    @Test
    public void shouldProcessMultipleSchemaFiles() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);

        final Path schema1 = sourceDir.resolve("a.json");
        final Path schema2 = sourceDir.resolve("b.json");
        Files.writeString(schema1, "{ \"id\": \"http://example.com/a.json\" }");
        Files.writeString(schema2, "{ \"id\": \"http://example.com/b.json\" }");

        final JSONObject inlinedSchema = new JSONObject("{}");
        when(jsonSchemaInliner.inline(any(JSONObject.class), any())).thenReturn(inlinedSchema);

        effectiveJsonSchemaGenerator.generate(List.of(schema1, schema2), sourceDir, Map.of(),
                tempDir.resolve("output"));

        verify(effectiveJsonSchemaWriter, org.mockito.Mockito.times(2)).write(any(), any());
    }

    @Test
    public void shouldSkipSchemaFileWithDuplicateKeys() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);
        final Path schemaFile = sourceDir.resolve("bad.json");
        Files.writeString(schemaFile, "{ \"name\": \"first\", \"name\": \"second\" }");

        effectiveJsonSchemaGenerator.generate(
                List.of(schemaFile), sourceDir, Map.of(), tempDir.resolve("output"));

        verify(effectiveJsonSchemaWriter, never()).write(any(), any());
    }

    @Test
    public void shouldThrowEffectiveJsonSchemaGenerationExceptionWhenWriterFails() throws Exception {
        final Path sourceDir = tempDir.resolve("source");
        Files.createDirectories(sourceDir);
        final Path schemaFile = sourceDir.resolve("person.json");
        Files.writeString(schemaFile, "{ \"id\": \"http://example.com/person.json\" }");

        final JSONObject inlinedSchema = new JSONObject("{}");
        when(jsonSchemaInliner.inline(any(JSONObject.class), any())).thenReturn(inlinedSchema);
        doThrow(new IOException("disk full")).when(effectiveJsonSchemaWriter).write(any(), any());

        final EffectiveJsonSchemaGenerationException exception = assertThrows(
                EffectiveJsonSchemaGenerationException.class,
                () -> effectiveJsonSchemaGenerator.generate(
                        List.of(schemaFile), sourceDir, Map.of(), tempDir.resolve("output")));

        assertThat(exception.getMessage().contains("person.json"), is(true));
    }
}
