package uk.gov.justice.services.common.converter;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;

import uk.gov.justice.services.common.converter.exception.ConverterException;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.IOException;
import java.util.UUID;

import javax.json.JsonArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListToJsonArrayConverterTest {

    private static final UUID ID_ONE = UUID.randomUUID();
    private static final UUID ID_TWO = UUID.randomUUID();
    private static final String NAME_ONE = "POJOONE";
    private static final String NAME_TWO = "POJOTWO";

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
        exception.expect(ConverterException.class);
        listToJsonArraysConverter.convert(null);

    }

    @Test
    public void shouldThrowExceptionOnIOException() throws JsonProcessingException {
        final ListToJsonArrayConverter<Pojo> listToJsonArraysConverter = new ListToJsonArrayConverter<Pojo>();
        listToJsonArraysConverter.mapper = mapperMock;
        listToJsonArraysConverter.stringToJsonObjectConverter = new StringToJsonObjectConverter();

        final Pojo pojoOne = new Pojo(null, null);
        doThrow(IOException.class).when(mapperMock).writeValueAsString(pojoOne);

        exception.expect(ConverterException.class);
        exception.expectCause(isA(IOException.class));

        listToJsonArraysConverter.convert(singletonList(pojoOne));

    }

    private JsonArray expectedJsonArray() {
        return createArrayBuilder()
                .add(createObjectBuilder().add("id", ID_ONE.toString()).add("name", NAME_ONE))
                .add(createObjectBuilder().add("id", ID_TWO.toString()).add("name", NAME_TWO))
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

    }
}
