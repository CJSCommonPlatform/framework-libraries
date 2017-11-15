package uk.gov.justice.schema.catalog.generation;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.json.Json.createReader;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;

import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;

public class SchemaIdParser {

    private final UrlConverter urlConverter;

    public SchemaIdParser(final UrlConverter urlConverter) {
        this.urlConverter = urlConverter;
    }

    public URI parse(final URL schemaFile) {

        try {
            final String schema = IOUtils.toString(schemaFile, UTF_8);
            try(final JsonReader reader = createReader(new StringReader(schema))) {
                final JsonObject jsonObject = reader.readObject();

                if (jsonObject.containsKey("id")) {
                    final String id = jsonObject.getString("id");
                    return urlConverter.toUri(id);
                }
            }

            throw new CatalogGenerationException(format("Failed to generate catalog. Schema '%s' has no id", schemaFile));

        } catch (final IOException e) {
            throw new CatalogGenerationException(format("Failed to extract id from schema file '%s'", schemaFile), e);
        }
    }
}
