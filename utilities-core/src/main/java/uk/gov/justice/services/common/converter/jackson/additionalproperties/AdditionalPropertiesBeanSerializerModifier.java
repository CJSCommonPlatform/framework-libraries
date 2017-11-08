package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static java.util.Collections.emptyList;
import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.ADDITIONAL_PROPERTIES_NAME;
import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.hasAdditionalPropertiesName;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class AdditionalPropertiesBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(final SerializationConfig config, final BeanDescription beanDesc, final JsonSerializer<?> serializer) {

        final List<BeanPropertyDefinition> bpdList = Optional.ofNullable(beanDesc.findProperties()).orElse(emptyList());

        if (bpdList.stream().anyMatch(hasAdditionalPropertiesName::test)) {
            return new AdditionalPropertiesSerializer((BeanSerializerBase) serializer, new String[]{ADDITIONAL_PROPERTIES_NAME});
        }
        return super.modifySerializer(config, beanDesc, serializer);
    }
}