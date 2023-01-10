package uk.gov.justice.services.common.converter.jackson.integerenum;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IntegerEnumDeserializerTest {

    @Mock
    private EnumResolver enumResolver;

    @Mock
    private EnumObjectUtil enumObjectUtil;

    private IntegerEnumDeserializer integerEnumDeserializer;

    @Before
    public void setup() {
        when(enumResolver.getRawEnums()).thenReturn(Age.values());
        integerEnumDeserializer = new IntegerEnumDeserializer(enumResolver, enumObjectUtil);
    }

    @Test
    public void shouldDeserializeEnum() throws Exception {
        final JsonParser jsonParser = mock(JsonParser.class);
        final int enumValue = 1;

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getIntValue()).thenReturn(enumValue);
        when(enumObjectUtil.findEnumObject(Age.ONE, enumValue)).thenReturn(Optional.of(Age.ONE));

        final Object result = integerEnumDeserializer.deserialize(jsonParser, mock(DeserializationContext.class));

        assertThat(result, is(Age.ONE));
    }

    public enum Age {
        ONE, TWO;
    }
}