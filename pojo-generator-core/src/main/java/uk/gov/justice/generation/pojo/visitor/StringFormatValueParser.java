package uk.gov.justice.generation.pojo.visitor;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

public class StringFormatValueParser {

    private static final String FORMAT_FIELD_NAME = "format";

    public Optional<String> parseFrom(final Reader reader, final String fieldName) {
        try {
            final JsonNode jsonNode = JsonLoader.fromReader(reader);
            final Optional<JsonNode> formatNode = Optional.ofNullable(jsonNode.get(FORMAT_FIELD_NAME));

            return formatNode.map(JsonNode::textValue);
        } catch (final IOException e) {
            throw new FailedToParseSchemaException(format("Failed to parse StringSchema format value for field name: %s", fieldName), e);
        }
    }
}
