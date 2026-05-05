package uk.gov.justice.schema.catalog.generation.effective;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads raw JSON schema content (keyed by schema ID) from two sources:
 * <ol>
 *   <li>Dependency JAR files — each JAR is inspected for a
 *       {@code META-INF/schema_catalog.json} catalog, and the schemas listed there
 *       are read directly from the JAR.</li>
 *   <li>A local source directory — every {@code .json} file that contains an {@code "id"}
 *       field is included.</li>
 * </ol>
 *
 * The resulting map ({@code schemaId -> rawJsonString}) is used by {@link JsonSchemaInliner}
 * to resolve external {@code $ref} values when building effective JSON schema documents.
 */
public class CatalogJsonSchemaLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogJsonSchemaLoader.class);

    private static final String SCHEMA_CATALOG_PATH = "META-INF/schema_catalog.json";

    private final ObjectMapper objectMapper;

    public CatalogJsonSchemaLoader(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Loads all known schemas from the given dependency JARs and the local source directory.
     *
     * @param dependencyJars  JAR files from the project's compile classpath
     * @param sourceDirectory the directory containing the current module's JSON schema files
     * @return a map of schema ID to raw JSON string
     */
    public Map<String, String> loadAllJsonSchemas(final List<File> dependencyJars,
                                                   final Path sourceDirectory) {
        final Map<String, String> schemasById = new HashMap<>();
        loadJsonSchemasFromJars(dependencyJars, schemasById);
        loadJsonSchemasFromSourceDirectory(sourceDirectory, schemasById);
        return schemasById;
    }

    private void loadJsonSchemasFromJars(final List<File> jars,
                                         final Map<String, String> schemasById) {
        for (final File jar : jars) {
            if (jar != null && jar.exists()) {
                loadJsonSchemasFromJar(jar, schemasById);
            }
        }
    }

    private void loadJsonSchemasFromJar(final File jar, final Map<String, String> schemasById) {
        try (final ZipFile zipFile = new ZipFile(jar)) {
            final ZipEntry catalogEntry = zipFile.getEntry(SCHEMA_CATALOG_PATH);
            if (catalogEntry != null) {
                loadSchemasViaCatalog(zipFile, catalogEntry, jar, schemasById);
            }
            fallbackScanJsonEntriesInJar(zipFile, jar, schemasById);
        } catch (final IOException e) {
            throw new EffectiveJsonSchemaGenerationException(
                    format("Failed to load JSON schemas from JAR '%s'", jar.getAbsolutePath()), e);
        }
    }

    private void loadSchemasViaCatalog(final ZipFile zipFile,
                                        final ZipEntry catalogEntry,
                                        final File jar,
                                        final Map<String, String> schemasById) throws IOException {
        final String catalogJson;
        try (final InputStream catalogStream = zipFile.getInputStream(catalogEntry)) {
            catalogJson = IOUtils.toString(catalogStream, UTF_8);
        }

        final Catalog catalog = objectMapper.readValue(catalogJson, Catalog.class);

        for (final Group group : catalog.getGroups()) {
            final String baseLocation = group.getBaseLocation() != null ? group.getBaseLocation() : "";
            for (final Schema schema : group.getSchemas()) {
                final String schemaPath = baseLocation + schema.getLocation();
                final ZipEntry schemaEntry = zipFile.getEntry(schemaPath);
                if (schemaEntry != null) {
                    try (final InputStream schemaStream = zipFile.getInputStream(schemaEntry)) {
                        schemasById.put(schema.getId(), IOUtils.toString(schemaStream, UTF_8));
                    }
                } else {
                    LOGGER.warn("Schema entry '{}' not found in JAR '{}'", schemaPath, jar.getName());
                }
            }
        }
    }

    private void fallbackScanJsonEntriesInJar(final ZipFile zipFile,
                                               final File jar,
                                               final Map<String, String> schemasById) {
        zipFile.stream()
                .filter(entry -> !entry.isDirectory())
                .filter(entry -> entry.getName().endsWith(".json"))
                .filter(entry -> !entry.getName().equals(SCHEMA_CATALOG_PATH))
                .forEach(entry -> {
                    try (final InputStream stream = zipFile.getInputStream(entry)) {
                        final String content = IOUtils.toString(stream, UTF_8);
                        final var jsonNode = objectMapper.readTree(content);
                        if (jsonNode.has("id") && jsonNode.get("id").isTextual()) {
                            // Validate with org.json before caching — JsonSchemaInliner uses
                            // org.json later and is strict about duplicate keys
                            try {
                                new JSONObject(content);
                                schemasById.putIfAbsent(jsonNode.get("id").asText(), content);
                            } catch (final JSONException e) {
                                LOGGER.warn("Skipping '{}' in JAR '{}': rejected by JSON parser ({})",
                                        entry.getName(), jar.getName(), e.getMessage());
                            }
                        }
                    } catch (final IOException e) {
                        LOGGER.warn("Skipping entry '{}' in JAR '{}': could not read as JSON ({})",
                                entry.getName(), jar.getName(), e.getMessage());
                    }
                });
    }

    private void loadJsonSchemasFromSourceDirectory(final Path sourceDirectory,
                                                     final Map<String, String> schemasById) {
        if (!Files.isDirectory(sourceDirectory)) {
            LOGGER.warn("Source directory '{}' does not exist or is not a directory", sourceDirectory);
            return;
        }

        try {
            Files.walk(sourceDirectory)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> loadJsonSchemaFile(path, schemasById));
        } catch (final IOException e) {
            throw new EffectiveJsonSchemaGenerationException(
                    format("Failed to scan source directory '%s'", sourceDirectory), e);
        }
    }

    private void loadJsonSchemaFile(final Path schemaFile, final Map<String, String> schemasById) {
        try {
            final String content = Files.readString(schemaFile);
            final var jsonNode = objectMapper.readTree(content);
            if (jsonNode.has("id") && jsonNode.get("id").isTextual()) {
                schemasById.put(jsonNode.get("id").asText(), content);
            }
        } catch (final IOException e) {
            LOGGER.warn("Skipping '{}': could not parse as JSON ({})", schemaFile, e.getMessage());
        }
    }
}
