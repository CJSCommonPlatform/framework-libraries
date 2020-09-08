package uk.gov.justice.services.test.utils.framework.api;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.justice.services.common.converter.ObjectToJsonValueConverter;
import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectConvertersFactory {

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    public JsonObjectToObjectConverter jsonObjectToObjectConverter() {
        return new JsonObjectToObjectConverter(objectMapper);
    }

    public ObjectToJsonObjectConverter objectToJsonObjectConverter() {
        return new ObjectToJsonObjectConverter(objectMapper);
    }

    public ObjectToJsonValueConverter objectToJsonValueConverter() {
        return new ObjectToJsonValueConverter(objectMapper);
    }

    public StringToJsonObjectConverter stringToJsonObjectConverter() {
        return new StringToJsonObjectConverter();
    }
}
