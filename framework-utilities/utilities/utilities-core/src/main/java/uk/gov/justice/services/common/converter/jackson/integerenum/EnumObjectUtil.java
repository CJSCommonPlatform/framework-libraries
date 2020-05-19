package uk.gov.justice.services.common.converter.jackson.integerenum;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class EnumObjectUtil implements Serializable {

    private static final long serialVersionUID = -7399009049859936180L;

    public Optional<Object> findEnumObject(final Object enumObject, final int enumValue) {
        final Field[] declaredFields = enumObject.getClass().getDeclaredFields();

        for (final Field field : declaredFields) {
            if (isIntegerFieldType(field)) {
                try {
                    field.setAccessible(true);
                    final int fieldValue = (int) field.get(enumObject);
                    if (fieldValue == enumValue) {
                        return of(enumObject);
                    }
                } catch (final IllegalAccessException e) {
                    //Do nothing
                }
            }
        }
        return empty();
    }

    public Optional<Integer> integerValueFromFieldOfEnum(final Enum<?> enumObject,
                                                         final String fieldName,
                                                         final SerializerProvider serializerProvider) throws JsonMappingException {
        try {
            final Field field = enumObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return Optional.of((Integer) field.get(enumObject));
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw serializerProvider.mappingException("Could not serialize enum object", ex);
        }
    }

    public Optional<String> integerFieldNameFrom(final Enum<?> enumObject) {
        final Field[] fields = enumObject.getClass().getDeclaredFields();

        for (final Field field : fields) {
            if (isIntegerFieldType(field)) {
                return Optional.of(field.getName());
            }
        }

        return empty();
    }

    private boolean isIntegerFieldType(final Field field) {
        return Integer.class.isAssignableFrom(field.getType());
    }
}
