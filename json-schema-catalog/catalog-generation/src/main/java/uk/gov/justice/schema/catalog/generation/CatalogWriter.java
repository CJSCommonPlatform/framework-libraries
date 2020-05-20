package uk.gov.justice.schema.catalog.generation;

import static java.lang.String.format;

import uk.gov.justice.schema.catalog.CatalogContext;
import uk.gov.justice.schema.catalog.domain.Catalog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Writes a {@link Catalog} Object as a json document
 */
public class CatalogWriter {

    private static final String LOG_MESSAGE = "\n" +
            "-----------------------------------------------------------------------------\n" +
            "Catalog Generation:\n" +
            "Generating catalog to:\n" +
            "'%s'\n" +
            "-----------------------------------------------------------------------------";
    
    private final ObjectMapper objectMapper;
    private final CatalogContext catalogContext;
    private final Logger logger;

    public CatalogWriter(
            final ObjectMapper objectMapper,
            final CatalogContext catalogContext,
            final Logger logger) {
        this.objectMapper = objectMapper;
        this.catalogContext = catalogContext;
        this.logger = logger;
    }

    /**
     * Write a {@link Catalog} Object as a json document
     *
     * @param catalog               The {@link Catalog}
     * @param catalogGenerationPath The location of where the new json document should be written
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void write(final Catalog catalog, final Path catalogGenerationPath) {


        final String catalogJson = writeAsString(catalog);
        final File outputDirectory = catalogGenerationPath.resolve(catalogContext.getCatalogLocation()).toFile();
        outputDirectory.mkdirs();

        final File outputFile = new File(outputDirectory, catalogContext.getCatalogFilename());

        logger.info(format(LOG_MESSAGE, outputFile.getAbsolutePath()));

        try (final FileWriter fileWriter = new FileWriter(outputFile)) {
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
