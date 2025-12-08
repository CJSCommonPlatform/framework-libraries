package uk.gov.justice.services.common.converter;

import static javax.json.JsonValue.NULL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

import uk.gov.justice.services.common.converter.exception.ConverterException;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.json.JsonArray;
import javax.json.JsonValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ObjectToJsonValueConverterTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "Pojo";
    private static final List<String> ATTRIBUTES = Arrays.asList("Attribute 1", "Attribute 2");
    private static final String ATTRIBUTE_1 = "Attribute 1";
    private static final String ATTRIBUTE_2 = "Attribute 2";
    private static final Boolean BOOL_FLAG = true;
    private static final String DATE_TIME = "2016-03-18T00:46:54.700Z";

    @Mock
    private ObjectMapper mapper;

    @Test
    public void shouldConvertPojoToJsonValue() throws Exception {
        final Pojo pojo = new Pojo(ID, NAME, BOOL_FLAG, ATTRIBUTES);
        final ObjectToJsonValueConverter objectToJsonValueConverter =
                new ObjectToJsonValueConverter(new ObjectMapperProducer().objectMapper());
        final JsonValue jsonValue = objectToJsonValueConverter.convert(pojo);

        assertThat(jsonValue, equalTo(expectedJsonValue()));
    }

    @Test
    public void shouldConvertPojoToJsonValue2() throws Exception {
        final Pojo pojo = new Pojo(ID, NAME, BOOL_FLAG, ATTRIBUTES);
        final ObjectToJsonValueConverter objectToJsonValueConverter =
                new ObjectToJsonValueConverter(new ObjectMapperProducer().objectMapper());

        final JsonValue jsonValue = objectToJsonValueConverter.convert(pojo);

        assertThat(jsonValue, equalTo(expectedJsonValue()));
    }

    @Test
    public void shouldConvertPojoWithZoneDateTimeToJsonValueWithOutTruncation() throws Exception {
        final PojoWithZoneDateTime pojo = new PojoWithZoneDateTime(ZonedDateTimes.fromString(DATE_TIME));
        final ObjectToJsonValueConverter objectToJsonValueConverter =
                new ObjectToJsonValueConverter(new ObjectMapperProducer().objectMapper());

        final JsonValue jsonValue = objectToJsonValueConverter.convert(pojo);

        assertThat(jsonValue, equalTo(
                getJsonBuilderFactory().createObjectBuilder()
                        .add("dateTime", DATE_TIME)
                        .build()));
    }

    @Test
    public void shouldConvertNullToJsonValueNull() throws Exception {
        final ObjectToJsonValueConverter objectToJsonValueConverter =
                new ObjectToJsonValueConverter(new ObjectMapperProducer().objectMapper());

        final JsonValue jsonValue = objectToJsonValueConverter.convert(null);

        assertThat(jsonValue, equalTo(NULL));
    }

    @Test
    public void shouldConvertListToJsonValue() throws Exception {
        final List<String> list = Arrays.asList(ATTRIBUTE_1, ATTRIBUTE_2);
        final ObjectToJsonValueConverter objectToJsonValueConverter =
                new ObjectToJsonValueConverter(new ObjectMapperProducer().objectMapper());
        final JsonValue jsonValue = objectToJsonValueConverter.convert(list);

        assertThat(jsonValue, equalTo(expectedJsonArray()));
    }

    @Test
    public void shouldThrowExceptionOnConversionError() throws JsonProcessingException {

        final ObjectToJsonValueConverter objectToJsonValueConverter = new ObjectToJsonValueConverter(mapper);

        final Pojo pojo = new Pojo(ID, NAME, false, ATTRIBUTES);
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(pojo);

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                objectToJsonValueConverter.convert(pojo)
        );

        assertThat(illegalArgumentException.getMessage(), is("Error while converting Some Pojo to JsonValue"));
        assertThat(illegalArgumentException.getCause(), is(instanceOf(JsonProcessingException.class)));
    }

    @Test
    public void shouldThrowExceptionOnNullResult() throws JsonProcessingException {
        final ObjectToJsonValueConverter objectToJsonValueConverter = new ObjectToJsonValueConverter(mapper);

        final Pojo pojo = new Pojo(ID, NAME, false, ATTRIBUTES);
        when(mapper.writeValueAsString(pojo)).thenReturn(null);

        assertThrows(ConverterException.class, () -> objectToJsonValueConverter.convert(pojo));
    }

    private JsonValue expectedJsonValue() {
        final JsonArray array = getJsonBuilderFactory().createArrayBuilder()
                .add("Attribute 1")
                .add("Attribute 2").build();

        return getJsonBuilderFactory().createObjectBuilder()
                .add("id", ID.toString())
                .add("name", NAME)
                .add("boolFlag", BOOL_FLAG)
                .add("attributes", array)
                .build();
    }

    private JsonValue expectedJsonArray() {
        return getJsonBuilderFactory().createArrayBuilder()
                .add(ATTRIBUTE_1)
                .add(ATTRIBUTE_2).build();
    }

    public static class Pojo {

        private final UUID id;
        private final String name;
        private final Boolean boolFlag;
        private final List<String> attributes;

        public Pojo(UUID id, String name, final Boolean boolFlag, List<String> attributes) {
            this.id = id;
            this.name = name;
            this.boolFlag = boolFlag;
            this.attributes = attributes;
        }

        public Boolean getBoolFlag() {
            return boolFlag;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getAttributes() {
            return attributes;
        }

        @Override
        public String toString() {
            return "Some Pojo";
        }
    }

    public static class PojoWithZoneDateTime {

        private final ZonedDateTime dateTime;

        public PojoWithZoneDateTime(final ZonedDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public ZonedDateTime getDateTime() {
            return dateTime;
        }
    }

}
