package uk.gov.justice.services.common.converter;

import static java.lang.String.format;
import static jakarta.json.Json.createArrayBuilder;

import uk.gov.justice.services.common.converter.exception.ConverterException;

import java.io.IOException;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a List of Type &lt;T&gt; to JsonArray
 *
 * @param <T> the type of objects in the List
 */
public class ListToJsonArrayConverter<T> implements Converter<List<T>, JsonArray> {

    @Inject
    ObjectMapper mapper;

    @Inject
    StringToJsonObjectConverter stringToJsonObjectConverter;

    public JsonArray convert(final List<T> sourceList) {
        final JsonArrayBuilder jsonArrayBuilder = createArrayBuilder();

        if (sourceList == null) {
            throw new ConverterException("Failed to convert Null List to JsonArray");
        }

        sourceList.forEach(object -> {
            try {
                jsonArrayBuilder.add(stringToJsonObjectConverter.convert(mapper.writeValueAsString(object)));
            } catch (IOException e) {
                throw new ConverterException(format("Error while converting list item %s to JsonValue", object), e);
            }
        });

        return jsonArrayBuilder.build();
    }

}
