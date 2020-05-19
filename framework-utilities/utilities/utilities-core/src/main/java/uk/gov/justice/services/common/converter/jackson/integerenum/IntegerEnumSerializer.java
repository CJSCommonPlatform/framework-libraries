package uk.gov.justice.services.common.converter.jackson.integerenum;

import static java.util.Optional.empty;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class IntegerEnumSerializer extends StdScalarSerializer<Enum<?>> {

    private static final long serialVersionUID = 1L;

    private final EnumObjectUtil enumObjectUtil;

    public IntegerEnumSerializer(final Class<?> enumClass, final EnumObjectUtil enumObjectUtil) {
        super(enumClass, false);
        this.enumObjectUtil = enumObjectUtil;
    }

    @Override
    public void serialize(final Enum<?> enumeration, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        final Optional<Integer> enumValue = getIntegerValue(enumeration, serializerProvider);

        if (enumValue.isPresent()) {
            jsonGenerator.writeNumber(enumValue.get());
        }
    }

    private Optional<Integer> getIntegerValue(final Enum<?> enumObject, final SerializerProvider serializerProvider) throws IOException {
        final Optional<String> fieldName = enumObjectUtil.integerFieldNameFrom(enumObject);

        if (fieldName.isPresent()) {
            return enumObjectUtil.integerValueFromFieldOfEnum(enumObject, fieldName.get(), serializerProvider);
        }

        return empty();
    }
}

