package uk.gov.justice.schema.catalog.generation.effective;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CatalogJsonSchemaLoaderTest {

    @TempDir
    Path tempDir;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CatalogJsonSchemaLoader catalogJsonSchemaLoader;

    @Test
    public void shouldLoadJsonSchemasFromSourceDirectory() throws Exception {
        final Path sourceDir = tempDir.resolve("schemas");
        Files.createDirectories(sourceDir);
        Files.writeString(sourceDir.resolve("address.json"),
                "{ \"id\": \"http://example.com/address.json\", \"type\": \"object\" }");

        final var mockNode = new ObjectMapper().readTree(
                "{ \"id\": \"http://example.com/address.json\", \"type\": \"object\" }");
        when(objectMapper.readTree(any(String.class))).thenReturn(mockNode);

        final Map<String, String> result = catalogJsonSchemaLoader.loadAllJsonSchemas(List.of(), sourceDir);

        assertThat(result, hasKey("http://example.com/address.json"));
    }

    @Test
    public void shouldSkipJsonFilesWithNoIdField() throws Exception {
        final Path sourceDir = tempDir.resolve("schemas");
        Files.createDirectories(sourceDir);
        Files.writeString(sourceDir.resolve("config.json"), "{ \"someKey\": \"someValue\" }");

        final var mockNode = new ObjectMapper().readTree("{ \"someKey\": \"someValue\" }");
        when(objectMapper.readTree(any(String.class))).thenReturn(mockNode);

        final Map<String, String> result = catalogJsonSchemaLoader.loadAllJsonSchemas(List.of(), sourceDir);

        assertThat(result, not(hasKey("someKey")));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void shouldSkipNonExistentJarFiles() throws Exception {
        final Path sourceDir = tempDir.resolve("schemas");
        Files.createDirectories(sourceDir);

        final File nonExistentJar = new File("/does/not/exist.jar");

        final Map<String, String> result = catalogJsonSchemaLoader.loadAllJsonSchemas(
                List.of(nonExistentJar), sourceDir);

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void shouldHandleNullJarEntryGracefully() throws Exception {
        final Path sourceDir = tempDir.resolve("schemas");
        Files.createDirectories(sourceDir);

        final Map<String, String> result = catalogJsonSchemaLoader.loadAllJsonSchemas(
                Arrays.asList((File) null), sourceDir);

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void shouldHandleNonExistentSourceDirectory() throws Exception {
        final Path nonExistentDir = tempDir.resolve("does-not-exist");

        final Map<String, String> result = catalogJsonSchemaLoader.loadAllJsonSchemas(
                List.of(), nonExistentDir);

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void shouldFallbackToScanningAllJsonEntriesWhenNoCatalogFound() throws Exception {
        final Path jarPath = tempDir.resolve("test-schemas.jar");
        final String schemaContent = "{ \"id\": \"http://example.com/fallback.json\", \"type\": \"object\" }";

        try (final ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(jarPath))) {
            zos.putNextEntry(new ZipEntry("schemas/fallback.json"));
            zos.write(schemaContent.getBytes(UTF_8));
            zos.closeEntry();
        }

        final var mockNode = new ObjectMapper().readTree(schemaContent);
        when(objectMapper.readTree(eq(schemaContent))).thenReturn(mockNode);

        final Path sourceDir = tempDir.resolve("src");
        Files.createDirectories(sourceDir);

        final Map<String, String> result = catalogJsonSchemaLoader.loadAllJsonSchemas(
                List.of(jarPath.toFile()), sourceDir);

        assertThat(result, hasKey("http://example.com/fallback.json"));
        assertThat(result.get("http://example.com/fallback.json"), is(schemaContent));
    }

    @Test
    public void shouldLoadMultipleSchemasFromSourceDirectory() throws Exception {
        final Path sourceDir = tempDir.resolve("schemas");
        final Path contextDir = sourceDir.resolve("context");
        Files.createDirectories(contextDir);

        Files.writeString(sourceDir.resolve("address.json"),
                "{ \"id\": \"http://example.com/address.json\", \"type\": \"object\" }");
        Files.writeString(contextDir.resolve("person.json"),
                "{ \"id\": \"http://example.com/person.json\", \"type\": \"object\" }");

        final ObjectMapper realMapper = new ObjectMapper();
        when(objectMapper.readTree(eq("{ \"id\": \"http://example.com/address.json\", \"type\": \"object\" }")))
                .thenReturn(realMapper.readTree("{ \"id\": \"http://example.com/address.json\", \"type\": \"object\" }"));
        when(objectMapper.readTree(eq("{ \"id\": \"http://example.com/person.json\", \"type\": \"object\" }")))
                .thenReturn(realMapper.readTree("{ \"id\": \"http://example.com/person.json\", \"type\": \"object\" }"));

        final Map<String, String> result = catalogJsonSchemaLoader.loadAllJsonSchemas(List.of(), sourceDir);

        assertThat(result, hasKey("http://example.com/address.json"));
        assertThat(result, hasKey("http://example.com/person.json"));
    }
}
