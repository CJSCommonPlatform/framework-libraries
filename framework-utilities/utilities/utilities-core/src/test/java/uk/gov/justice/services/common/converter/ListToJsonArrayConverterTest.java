package uk.gov.justice.services.common.converter;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

import uk.gov.justice.services.common.converter.exception.ConverterException;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.util.UUID;

import javax.json.JsonArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ListToJsonArrayConverterTest {

    private static final UUID ID_ONE = UUID.randomUUID();
    private static final UUID ID_TWO = UUID.randomUUID();
    private static final String NAME_ONE = "POJOONE";
    private static final String NAME_TWO = "POJOTWO";

    @Mock
    private ObjectMapper mapperMock;

    @Test
    public void shouldConvertListToJsonArray() {
        final ListToJsonArrayConverter<Pojo> listToJsonArraysConverter = new ListToJsonArrayConverter<Pojo>();
        listToJsonArraysConverter.mapper = new ObjectMapperProducer().objectMapper();
        listToJsonArraysConverter.stringToJsonObjectConverter = new StringToJsonObjectConverter();

        final Pojo pojoOne = new Pojo(ID_ONE, NAME_ONE);
        final Pojo pojoTwo = new Pojo(ID_TWO, NAME_TWO);
        final JsonArray jsonArray = listToJsonArraysConverter.convert(asList(pojoOne, pojoTwo));
        assertThat(jsonArray, equalTo(expectedJsonArray()));

    }

    @Test
    public void shouldThrowExceptionOnConversionError() {
        final ListToJsonArrayConverter<Pojo> listToJsonArraysConverter = new ListToJsonArrayConverter<Pojo>();

        final ConverterException converterException = assertThrows(ConverterException.class, () ->
                listToJsonArraysConverter.convert(null)
        );

        assertThat(converterException.getMessage(), is("Failed to convert Null List to JsonArray"));
    }

    @Test
    public void shouldThrowExceptionOnIOException() throws JsonProcessingException {

        final ListToJsonArrayConverter<Pojo> listToJsonArraysConverter = new ListToJsonArrayConverter<Pojo>();
        listToJsonArraysConverter.mapper = mapperMock;
        listToJsonArraysConverter.stringToJsonObjectConverter = new StringToJsonObjectConverter();

        final Pojo pojoOne = new Pojo(null, null);
        doThrow(JsonProcessingException.class).when(mapperMock).writeValueAsString(pojoOne);

        final ConverterException converterException = assertThrows(ConverterException.class, () ->
                listToJsonArraysConverter.convert(singletonList(pojoOne))
        );

        assertThat(converterException.getMessage(), is("Error while converting list item Some Pojo to JsonValue"));
        assertThat(converterException.getCause(), is(instanceOf(JsonProcessingException.class)));
    }

    private JsonArray expectedJsonArray() {
        return getJsonBuilderFactory().createArrayBuilder()
                .add(getJsonBuilderFactory().createObjectBuilder().add("id", ID_ONE.toString()).add("name", NAME_ONE))
                .add(getJsonBuilderFactory().createObjectBuilder().add("id", ID_TWO.toString()).add("name", NAME_TWO))
                .build();
    }

    public static class Pojo {

        private final UUID id;
        private final String name;

        public Pojo(final UUID id, final String name) {
            this.id = id;
            this.name = name;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Some Pojo";
        }
    }
}
