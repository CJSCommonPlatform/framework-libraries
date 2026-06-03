package uk.gov.justice.services.common.converter.jackson.integerenum;

import static com.fasterxml.jackson.databind.util.EnumResolver.constructFor;
import static com.fasterxml.jackson.databind.util.EnumResolver.constructUsingToString;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.util.EnumResolver;


public class IntegerEnumBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyEnumDeserializer(
            final DeserializationConfig config,
            final JavaType type,
            final BeanDescription beanDesc,
            final JsonDeserializer<?> deserializer) {

        final EnumResolver enumResolver = constructFor(config, beanDesc.getBeanClass());
        final EnumResolver toStringResolver = constructUsingToString(config, beanDesc.getBeanClass());

        return new IntegerEnumDeserializer(
                enumResolver,
                toStringResolver,
                new EnumObjectUtil());
    }
}
