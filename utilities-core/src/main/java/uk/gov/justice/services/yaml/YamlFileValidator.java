package uk.gov.justice.services.yaml;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URL;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;

public class YamlFileValidator {
    private final YamlToJsonObjectConverter yamlToJsonObjectConverter;
    private final YamlSchemaLoader yamlSchemaLoader;

    public YamlFileValidator(final YamlToJsonObjectConverter yamlToJsonObjectConverter,
                             final YamlSchemaLoader yamlSchemaLoader) {
        this.yamlToJsonObjectConverter = yamlToJsonObjectConverter;
        this.yamlSchemaLoader = yamlSchemaLoader;
    }

    public void validate(final String schemaFileLocation, final URL yamlUrl) {

        final Schema schema = load(schemaFileLocation);

        final JSONObject yamlAsJson = yamlToJsonObjectConverter.convert(yamlUrl);

        try {
            schema.validate(yamlAsJson);
        } catch (final ValidationException e) {
            throw new YamlValidationException(format("'%s' failed validation against schema '%s'. Errors: %s", schemaFileLocation, yamlUrl, e.getAllMessages()), e);
        }
    }

    private Schema load(final String schemaFileLocation) {
        try {
            return yamlSchemaLoader.loadSchema(schemaFileLocation);
        } catch (final IOException ex) {
            throw new YamlParserException(format("Unable to load JSON schema %s from classpath", schemaFileLocation), ex);
        }
    }
}
