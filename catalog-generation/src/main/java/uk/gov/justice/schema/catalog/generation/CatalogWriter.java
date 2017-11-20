package uk.gov.justice.schema.catalog.generation;

import static java.lang.String.format;

import uk.gov.justice.schema.catalog.domain.Catalog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

public class CatalogWriter {

    private static final String CATALOG_SUB_DIRECTORY = "json/schema";

    private final ObjectMapper objectMapper;
    private final CatalogGenerationContext catalogGenerationContext;

    public CatalogWriter(final ObjectMapper objectMapper, final CatalogGenerationContext catalogGenerationContext) {
        this.objectMapper = objectMapper;
        this.catalogGenerationContext = catalogGenerationContext;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void write(final Catalog catalog, final Path catalogGenerationPath) {


        final String catalogJson = writeAsString(catalog);
        final File outputDirectory = catalogGenerationPath.resolve(CATALOG_SUB_DIRECTORY).toFile();
        outputDirectory.mkdirs();

        final File outputFile = new File(outputDirectory, catalogGenerationContext.getCatalogFilename());
        try(final FileWriter fileWriter = new FileWriter(outputFile)) {
            IOUtils.write(catalogJson, fileWriter);
        } catch (final IOException e) {
            throw new CatalogGenerationException(format("Failed to write catalog to '%s'", outputFile.getAbsolutePath()), e);
        }
    }

    private String writeAsString(final Catalog catalog) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(catalog);
        } catch (final JsonProcessingException e) {
            throw new CatalogGenerationException("Failed to write Catalog as json string", e);
        }
    }
}
