package uk.gov.justice.services.common.converter.jackson.integerenum;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.jackson.integerenum.EnumObjectUtilTest.Age.FOURTY;
import static uk.gov.justice.services.common.converter.jackson.integerenum.EnumObjectUtilTest.Age.THIRTY;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

public class EnumObjectUtilTest {

    @Test
    public void shouldFindEnumObject() throws Exception {

        final Age age = THIRTY;
        final Optional<Object> enumerationFound = new EnumObjectUtil().findEnumObject(age, 30);

        assertTrue(enumerationFound.isPresent());
    }

    @Test
    public void shouldNotFindEnumObject() throws Exception {

        final Age age = FOURTY;
        final Optional<Object> enumerationFound = new EnumObjectUtil().findEnumObject(age, 30);

        assertFalse(enumerationFound.isPresent());
    }

    @Test
    public void shouldReturnIntegerValueFromEnumField() throws Exception {
        final Optional<Integer> result = new EnumObjectUtil()
                .integerValueFromFieldOfEnum(Age.THIRTY, "age", mock(SerializerProvider.class));

        assertThat(result, is(Optional.of(30)));
    }

    @Test
    public void shouldThrowJsonMappingExceptionWhenNoSuchField() throws Exception {
        final SerializerProvider serializerProvider = mock(SerializerProvider.class);
        final JsonMappingException jsonMappingException = mock(JsonMappingException.class);

        when(serializerProvider.mappingException(ArgumentMatchers.any(String.class), ArgumentMatchers.any(NoSuchFieldException.class)))
                .thenReturn(jsonMappingException);

        try {
            new EnumObjectUtil()
                    .integerValueFromFieldOfEnum(Age.THIRTY, "unknownField", serializerProvider);

            fail();
        } catch (final JsonMappingException e) {
            assertThat(e, sameInstance(jsonMappingException));
        }
    }

    @Test
    public void shouldReturnIntegerFieldNameFromEnum() {
        final Optional<String> result = new EnumObjectUtil().integerFieldNameFrom(Age.THIRTY);

        assertThat(result, is(Optional.of("age")));
    }

    @Test
    public void shouldReturnOptionalEmptyIfNoIntegerFieldPresent() {
        final Optional<String> result = new EnumObjectUtil().integerFieldNameFrom(NoIntegerPresent.NO_INTEGER_PRESENT);

        assertThat(result, is(Optional.empty()));
    }

    public enum Age {
        THIRTY(30),
        FOURTY(40);

        private final Integer age;

        private Age(Integer age) {
            this.age = age;
        }
    }

    public enum NoIntegerPresent {
        NO_INTEGER_PRESENT;
    }
}