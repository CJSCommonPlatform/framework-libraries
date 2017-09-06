package uk.gov.justice.generation.pojo.visitor;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

class ReferenceValueParser {

    private static final String REFERENCE_VALUE_FIELD_NAME = "$ref";

    String parseFrom(final Reader reader, final String fieldName) {
        try {
            final JsonNode jsonNode = JsonLoader.fromReader(reader);
            return jsonNode.get(REFERENCE_VALUE_FIELD_NAME).textValue();
        } catch (final IOException e) {
            throw new FailedToParseSchemaException(format("Failed to parse ReferenceSchema $ref value for field name: %s", fieldName), e);
        }
    }
}
