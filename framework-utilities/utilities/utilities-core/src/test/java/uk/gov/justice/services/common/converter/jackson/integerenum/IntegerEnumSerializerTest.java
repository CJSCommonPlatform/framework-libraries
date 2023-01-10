package uk.gov.justice.services.common.converter.jackson.integerenum;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

public class IntegerEnumSerializerTest {

    @Test
    public void shouldSerializeIntegerEnum() throws Exception {
        final EnumObjectUtil enumObjectUtil = mock(EnumObjectUtil.class);
        final JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        final SerializerProvider serializerProvider = mock(SerializerProvider.class);

        final IntegerEnumSerializer integerEnumSerializer = new IntegerEnumSerializer(Age.class, enumObjectUtil);

        when(enumObjectUtil.integerFieldNameFrom(Age.ONE)).thenReturn(Optional.of("number"));
        when(enumObjectUtil.integerValueFromFieldOfEnum(Age.ONE, "number", serializerProvider)).thenReturn(Optional.of(1));

        integerEnumSerializer.serialize(Age.ONE, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(1);
    }

    @Test
    public void shouldNotSerializeIntegerEnumIfNoIntegerValue() throws Exception {
        final EnumObjectUtil enumObjectUtil = mock(EnumObjectUtil.class);
        final JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        final SerializerProvider serializerProvider = mock(SerializerProvider.class);

        final IntegerEnumSerializer integerEnumSerializer = new IntegerEnumSerializer(Age.class, enumObjectUtil);

        when(enumObjectUtil.integerFieldNameFrom(Age.ONE)).thenReturn(Optional.empty());

        integerEnumSerializer.serialize(Age.ONE, jsonGenerator, serializerProvider);

        verifyNoMoreInteractions(jsonGenerator);
    }

    public enum Age {
        ONE(1), TWO(2);

        private final Integer number;

        Age(final Integer number) {
            this.number = number;
        }
    }
}