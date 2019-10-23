package uk.gov.justice.services.common.converter;

import static java.lang.String.format;
import static uk.gov.justice.services.common.converter.JSONObjectValueObfuscator.obfuscated;

import uk.gov.justice.services.common.converter.exception.ConverterException;

import java.io.IOException;

import javax.inject.Inject;
import javax.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts JsonObject to the given Pojo type.
 */
public class JsonObjectToObjectConverter implements TypedConverter<JsonObject, Object> {

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public <R> R convert(final JsonObject source, final Class<R> clazz) {
        try {
            final R object = objectMapper.readValue(objectMapper.writeValueAsString(source), clazz);

            if (object == null) {
                throw new ConverterException(format("Error while converting to %s from json:[%s]", clazz.getName(), obfuscated(source)));
            }

            return object;
        } catch (final IOException e) {
            throw new IllegalArgumentException(format("Error while converting to %s from json (obfuscated):[%s]", clazz.getName(), obfuscated(source)));
        }
    }
}
