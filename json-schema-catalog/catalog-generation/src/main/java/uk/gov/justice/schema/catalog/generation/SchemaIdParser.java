package uk.gov.justice.schema.catalog.generation;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonReaderFactory;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Loads a json schema file and extracts the schema id. A {@link CatalogGenerationException} is
 * thrown if no id is found in the schema file.
 */
public class SchemaIdParser {

    private final UrlConverter urlConverter;
    private final Logger logger;

    public SchemaIdParser(final UrlConverter urlConverter, final Logger logger) {
        this.urlConverter = urlConverter;
        this.logger = logger;
    }

    /**
     * Extracts the id from a json schema file. A {@link CatalogGenerationException} is
     * thrown if no id is found in the schema file, or if there is a problem reading the file.
     *
     * @param schemaFile A {@link URL} to the json schema file
     * @return The id contained in the json schema file.
     */
    public Optional<URI> parse(final URL schemaFile) {

        try {
            final String schema = IOUtils.toString(schemaFile, UTF_8);
            try(final JsonReader reader = getJsonReaderFactory().createReader(new StringReader(schema))) {
                final JsonObject jsonObject = reader.readObject();

                if (jsonObject.containsKey("id")) {
                    final String id = jsonObject.getString("id");
                    return of(urlConverter.toUri(id));
                }
            }

            logger.warn(format("Failed to generate catalog. Schema '%s' has no id", schemaFile));

            return empty();

        } catch (final IOException e) {
            throw new CatalogGenerationException(format("Failed to extract id from schema file '%s'", schemaFile), e);
        } catch (final JsonParsingException e) {
            throw new CatalogGenerationException(format("Failed to parse schema file '%s'", schemaFile), e);
        }
    }
}
