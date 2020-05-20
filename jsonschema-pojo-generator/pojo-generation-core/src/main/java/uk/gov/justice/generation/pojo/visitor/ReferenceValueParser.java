package uk.gov.justice.generation.pojo.visitor;

import static java.lang.String.format;
import static uk.gov.justice.generation.pojo.visitor.ReferenceValue.fromReferenceValueString;

import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

/**
 * Allows parsing of the $ref value from a reference schema.
 */
public class ReferenceValueParser {

    private static final String REFERENCE_VALUE_FIELD_NAME = "$ref";

    /**
     * Convert from a Reader to a JsonNode, and retrieve the $ref value
     *
     * @param reader    the Reader to read for the JsonNode
     * @param fieldName the field name to be used if exception is thrown
     * @return the $ref value
     * @throws FailedToParseSchemaException if an IOException occurs while reading the Json
     */
    public ReferenceValue parseFrom(final Reader reader, final String fieldName) {
        try {
            final JsonNode jsonNode = JsonLoader.fromReader(reader);
            final String referenceValueString = jsonNode.get(REFERENCE_VALUE_FIELD_NAME).textValue();

            return fromReferenceValueString(referenceValueString);
            
        } catch (final IOException e) {
            throw new FailedToParseSchemaException(format("Failed to parse ReferenceSchema $ref value for field name: %s", fieldName), e);
        }
    }
}
