package uk.gov.justice.services.common.converter.jackson.additionalproperties;


import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.ADDITIONAL_PROPERTIES_NAME;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import org.apache.commons.lang3.reflect.FieldUtils;


public class AdditionalPropertiesDeserializer extends BeanDeserializer {

    public AdditionalPropertiesDeserializer(final BeanDeserializerBase originalDeserializer) {
        super(originalDeserializer);
    }

    @Override
    public void handleUnknownProperty(final JsonParser jsonParser,
                                      final DeserializationContext context,
                                      final Object bean,
                                      final String propName) throws IOException {
        final Object value = deserializeFromType(jsonParser, context, jsonParser.getCurrentToken());

        Map<String, Object> additionalPropertiesMap;

        try {
            final Field additionalPropertiesMapField = FieldUtils.getDeclaredField(bean.getClass(), ADDITIONAL_PROPERTIES_NAME, true);

            if (isValidAdditionalPropertiesField(additionalPropertiesMapField)) {

                additionalPropertiesMap = (Map<String, Object>) FieldUtils.readDeclaredField(bean, ADDITIONAL_PROPERTIES_NAME, true);
                if (additionalPropertiesMap == null) {
                    additionalPropertiesMap = new HashMap<>();
                    FieldUtils.writeDeclaredField(bean, ADDITIONAL_PROPERTIES_NAME, additionalPropertiesMap, true);
                }
                additionalPropertiesMap.put(propName, value);
            } else {
                throw context.mappingException("Expected target object to have additionalProperties attribute! [" + bean.getClass() + "]");
            }
        } catch (final Exception ex) {
            throw context.mappingException("Couldn't add [" + propName + "] to additionalProperties attribute!", ex);
        }
    }

    private boolean isValidAdditionalPropertiesField(final Field mapField) {
        if (mapField != null) {
            final ParameterizedType mapFieldParameterizedType = (ParameterizedType) mapField.getGenericType();
            final Class<?> mapKey = (Class<?>) mapFieldParameterizedType.getActualTypeArguments()[0];
            final Class<?> mapValue = (Class<?>) mapFieldParameterizedType.getActualTypeArguments()[1];
            if (mapKey == String.class && mapValue == Object.class) {
                return true;
            }
        }
        return false;
    }

    private Object deserializeFromType(final JsonParser jsonParser, final DeserializationContext context, final JsonToken jsonToken) throws IOException {
        final Object obj;
        switch (jsonToken) {
            case VALUE_STRING:
                obj = jsonParser.getValueAsString();
                break;
            case VALUE_NUMBER_INT:
                obj = jsonParser.getValueAsInt();
                break;
            case VALUE_NUMBER_FLOAT:
                obj = (float) jsonParser.getValueAsDouble();
                break;
            case VALUE_TRUE:
            case VALUE_FALSE:
                obj = jsonParser.getValueAsBoolean();
                break;
            case VALUE_NULL:
                obj = null;
                break;
            default:
                throw context.mappingException(this.handledType());
        }
        return obj;
    }
}
