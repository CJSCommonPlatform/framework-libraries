package uk.gov.justice.services.common.converter;

import static java.lang.String.format;

import uk.gov.justice.services.common.converter.exception.ConverterException;

import java.io.IOException;

import javax.enterprise.inject.Vetoed;
import javax.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a Pojo to a JsonObject
 */
@Vetoed
public class ObjectToJsonObjectConverter implements Converter<Object, JsonObject> {

    private final ObjectMapper mapper;

    /**
     * @see JsonObjectConvertersProducer
     * @deprecated Please use the parameterised constructor in your tests. This constructor is never
     * used in production code as it is always injected and will be removed soon.
     * <p>
     * There is now a Producer for CDI injection:
     */
    @Deprecated
    public ObjectToJsonObjectConverter() {
        mapper = null;
    }

    public ObjectToJsonObjectConverter(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JsonObject convert(final Object source) {
        try {
            final String json = mapper.writeValueAsString(source);

            if (json != null) {
                final JsonObject jsonObject = mapper.readValue(json, JsonObject.class);

                if (jsonObject != null) {
                    return jsonObject;
                }

            }
            throw new ConverterException(format("Failed to convert %s to JsonObject", source));

        } catch (IOException e) {
            throw new IllegalArgumentException(format("Error while converting %s toJsonObject", source), e);
        }
    }

}
