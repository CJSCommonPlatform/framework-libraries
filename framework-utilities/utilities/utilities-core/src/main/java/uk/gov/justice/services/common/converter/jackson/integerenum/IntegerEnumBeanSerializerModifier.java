package uk.gov.justice.services.common.converter.jackson.integerenum;

import static java.util.Arrays.stream;

import java.lang.reflect.Field;
import java.util.Optional;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;


public class IntegerEnumBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifyEnumSerializer(final SerializationConfig config,
                                                  final JavaType valueType,
                                                  final BeanDescription beanDesc,
                                                  final JsonSerializer<?> serializer) {

        final EnumValues enumValues = ((EnumSerializer) serializer).getEnumValues();
        final Optional integerEnum = isIntegerEnum(enumValues);

        if (integerEnum.isPresent()) {
            return new IntegerEnumSerializer(
                    enumValues.getEnumClass(),
                    new EnumObjectUtil());
        }
        return super.modifySerializer(config, beanDesc, serializer);
    }

    private Optional isIntegerEnum(final EnumValues enumValues) {
        final Class<Enum<?>> enumClass = enumValues.getEnumClass();

        final Field[] declaredFields = enumClass.getDeclaredFields();

        return stream(declaredFields)
                .filter(field -> Integer.class.isAssignableFrom(field.getType()))
                .findFirst();
    }

}