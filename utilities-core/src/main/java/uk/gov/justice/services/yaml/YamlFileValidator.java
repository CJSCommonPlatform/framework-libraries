package uk.gov.justice.services.yaml;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URL;

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

        final JSONObject yamlAsJson = yamlToJsonObjectConverter.convert(yamlUrl);
        try {
            yamlSchemaLoader.loadSchema(schemaFileLocation).validate(yamlAsJson);
        } catch (final IOException ex) {
            throw new YamlParserException(format("Unable to load JSON schema %s from classpath", schemaFileLocation), ex);
        }
    }
}
