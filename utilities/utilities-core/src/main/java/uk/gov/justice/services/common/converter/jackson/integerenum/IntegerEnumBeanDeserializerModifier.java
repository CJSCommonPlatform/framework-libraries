package uk.gov.justice.services.common.converter.jackson.integerenum;

import static com.fasterxml.jackson.databind.AnnotationIntrospector.nopInstance;
import static com.fasterxml.jackson.databind.util.EnumResolver.constructFor;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class IntegerEnumBeanDeserializerModifier extends BeanDeserializerModifier {

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> modifyEnumDeserializer(
            final DeserializationConfig config,
            final JavaType type,
            final BeanDescription beanDesc,
            final JsonDeserializer<?> deserializer) {

        final Class<Enum<?>> enumClass = (Class<Enum<?>>) type.getRawClass();

        final EnumResolver enumResolver = constructFor(enumClass, nopInstance());

        return new IntegerEnumDeserializer(
                enumResolver,
                new EnumObjectUtil());
    }
}
