package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static java.util.Collections.emptyList;
import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.hasAdditionalPropertiesName;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;


public class AdditionalPropertiesBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(final DeserializationConfig config,
                                                  final BeanDescription beanDesc,
                                                  final JsonDeserializer<?> deserializer) {

        final List<BeanPropertyDefinition> bpdList = Optional.ofNullable(beanDesc.findProperties()).orElse(emptyList());

        if (bpdList.stream().anyMatch(hasAdditionalPropertiesName::test)) {
            return new AdditionalPropertiesDeserializer((BeanDeserializerBase) deserializer);
        }
        return super.modifyDeserializer(config, beanDesc, deserializer);
    }
}