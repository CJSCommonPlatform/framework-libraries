package uk.gov.justice.schema.catalog.generation.effective;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

/**
 * Writes a generated effective JSON schema document to a file, creating any
 * intermediate parent directories as needed.
 */
public class EffectiveJsonSchemaWriter {

    public void write(final JSONObject effectiveJsonSchema, final Path outputPath) throws IOException {
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, effectiveJsonSchema.toString(2), UTF_8);
    }
}
