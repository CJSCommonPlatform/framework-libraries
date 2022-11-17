package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.ADDITIONAL_PROPERTIES_NAME;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class AdditionalPropertiesSerializer extends BeanSerializerBase {


    // deprecated as the super constructor is deprecated.
    // TODO remove this constructor and use the non deprecated version
    @Deprecated
    public AdditionalPropertiesSerializer(final BeanSerializerBase source, final String[] toIgnore) {
        super(source, toIgnore);
    }

    // Use this constructor from now on
    public AdditionalPropertiesSerializer(final BeanSerializerBase source, final Set<String> toIgnore) {
        super(source, toIgnore);
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(final ObjectIdWriter objectIdWriter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeanSerializerBase withIgnorals(final Set<String> toIgnore) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected BeanSerializerBase withByNameInclusion(final Set<String> set_1, final Set<String> set_2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeanSerializerBase asArraySerializer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeanSerializerBase withFilterId(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected BeanSerializerBase withProperties(final BeanPropertyWriter[] beanPropertyWriters, final BeanPropertyWriter[] beanPropertyWriters1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void serialize(final Object bean, final JsonGenerator jsonGenerator, final SerializerProvider provider) throws IOException {

        jsonGenerator.writeStartObject();
        try {
            final Field apField = bean.getClass().getDeclaredField(ADDITIONAL_PROPERTIES_NAME);
            apField.setAccessible(true);

            final Map<String, Object> apMap = (Map<String, Object>) apField.get(bean);

            if (apMap != null && !apMap.isEmpty()) {
                setAdditionalPropertiesMap(jsonGenerator, apMap);
            }

        } catch (final NoSuchFieldException | IllegalAccessException | IOException e) {
            throw provider.mappingException("Could not serialize object with additional properties ", e);
        }

        serializeFields(bean, jsonGenerator, provider);

        jsonGenerator.writeEndObject();
    }

    private void setAdditionalPropertiesMap(final JsonGenerator jsonGenerator, final Map<String, Object> apMap) throws IOException {
        for (Map.Entry<String, Object> entry : apMap.entrySet()) {
            jsonGenerator.writeObjectField(entry.getKey(), entry.getValue());
        }
    }
}

