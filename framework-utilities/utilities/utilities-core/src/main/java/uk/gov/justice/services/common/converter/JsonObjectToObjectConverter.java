package uk.gov.justice.services.common.converter;

import static java.lang.String.format;
import static uk.gov.justice.services.common.converter.JSONObjectValueObfuscator.obfuscated;

import uk.gov.justice.services.common.converter.exception.ConverterException;

import java.io.IOException;

import javax.enterprise.inject.Vetoed;
import javax.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts JsonObject to the given Pojo type.
 */
@Vetoed
public class JsonObjectToObjectConverter implements TypedConverter<JsonObject, Object> {

    private final ObjectMapper objectMapper;

    /**
     * @deprecated Please use the parameterised constructor in your tests.
     * This constructor is never used in production code as it is always
     * injected and will be removed soon.
     *
     * There is now a Producer for CDI injection:
     * @see JsonObjectConvertersProducer
     */
    @Deprecated
    public JsonObjectToObjectConverter() {
        objectMapper = null;
    }

    public JsonObjectToObjectConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
