package uk.gov.justice.schema.catalog.generation.effective;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates effective JSON schema documents for a set of source schema files.
 * For each schema file, all external {@code $ref} references are resolved and inlined
 * into a single self-contained document, which is written to the output directory
 * preserving the relative path structure of the source directory.
 */
public class EffectiveJsonSchemaGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EffectiveJsonSchemaGenerator.class);

    private final JsonSchemaInliner jsonSchemaInliner;
    private final EffectiveJsonSchemaWriter effectiveJsonSchemaWriter;

    public EffectiveJsonSchemaGenerator(final JsonSchemaInliner jsonSchemaInliner,
                                        final EffectiveJsonSchemaWriter effectiveJsonSchemaWriter) {
        this.jsonSchemaInliner = jsonSchemaInliner;
        this.effectiveJsonSchemaWriter = effectiveJsonSchemaWriter;
    }

    /**
     * Generates effective JSON schema documents for each of the given source schema files.
     *
     * @param schemaFiles       the source schema files to process
     * @param sourceBaseDirectory the root directory used to compute output relative paths
     * @param allSchemasById    a map of schema ID to raw JSON for all known schemas
     * @param outputDirectory   directory under which effective schemas are written
     */
    public void generate(final List<Path> schemaFiles,
                         final Path sourceBaseDirectory,
                         final Map<String, String> allSchemasById,
                         final Path outputDirectory) {
        for (final Path schemaFile : schemaFiles) {
            generateEffectiveJsonSchema(schemaFile, sourceBaseDirectory, allSchemasById, outputDirectory);
        }
    }

    private void generateEffectiveJsonSchema(final Path schemaFile,
                                             final Path sourceBaseDirectory,
                                             final Map<String, String> allSchemasById,
                                             final Path outputDirectory) {
        try {
            final String rawJson = Files.readString(schemaFile);
            final JSONObject schema;
            try {
                schema = new JSONObject(rawJson);
            } catch (final JSONException e) {
                LOGGER.warn("Skipping '{}': could not parse as JSON schema ({})", schemaFile, e.getMessage());
                return;
            }
            final JSONObject effectiveJsonSchema = jsonSchemaInliner.inline(schema, allSchemasById);

            final Path relativePath = sourceBaseDirectory.relativize(schemaFile);
            final Path outputPath = outputDirectory.resolve(relativePath);

            effectiveJsonSchemaWriter.write(effectiveJsonSchema, outputPath);
        } catch (final IOException e) {
            throw new EffectiveJsonSchemaGenerationException(
                    format("Failed to generate effective JSON schema for '%s'", schemaFile), e);
        }
    }
}
