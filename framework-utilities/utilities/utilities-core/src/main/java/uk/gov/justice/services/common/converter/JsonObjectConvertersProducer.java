package uk.gov.justice.services.common.converter;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectConvertersProducer {

    @Inject
    private ObjectMapper objectMapper;

    @Produces
    public  JsonObjectToObjectConverter jsonObjectToObjectConverter() {
        return new JsonObjectToObjectConverter(objectMapper);
    }

    @Produces
    public ObjectToJsonObjectConverter objectToJsonObjectConverter() {
        return new ObjectToJsonObjectConverter(objectMapper);
    }

    @Produces
    public ObjectToJsonValueConverter objectToJsonValueConverter() {
        return new ObjectToJsonValueConverter(objectMapper);
    }
}
