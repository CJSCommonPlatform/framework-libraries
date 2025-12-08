package uk.gov.justice.services.common.converter;

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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ObjectToJsonObjectConverterTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "Pojo";
    private static final List<String> ATTRIBUTES = Arrays.asList("Attribute 1", "Attribute 2");

    @Spy
    private ObjectMapper mapper = new ObjectMapperProducer().objectMapper();

    @InjectMocks
    private ObjectToJsonObjectConverter objectToJsonObjectConverter;

    @Test
    public void shouldConvertPojoToJsonObject() throws Exception {
        final Pojo pojo = new Pojo(ID, NAME, ATTRIBUTES);

        final JsonObject jsonObject = objectToJsonObjectConverter.convert(pojo);

        assertThat(jsonObject, equalTo(expectedJsonObject()));
    }

    @Test
    public void shouldThrowExceptionOnConversionError() throws JsonProcessingException {

        final Pojo pojo = new Pojo(ID, NAME, ATTRIBUTES);
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(pojo);

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                objectToJsonObjectConverter.convert(pojo)
        );

        assertThat(illegalArgumentException.getMessage(), is("Error while converting Some Pojo toJsonObject"));
        assertThat(illegalArgumentException.getCause(), is(instanceOf(JsonProcessingException.class)));
    }

    @Test
    public void shouldThrowExceptionOnNullResult() throws JsonProcessingException {

        final Pojo pojo = new Pojo(ID, NAME, ATTRIBUTES);
        when(mapper.writeValueAsString(pojo)).thenReturn(null);

        final ObjectToJsonObjectConverter objectToJsonObjectConverter = new ObjectToJsonObjectConverter(mapper);

        assertThrows(ConverterException.class, () -> objectToJsonObjectConverter.convert(pojo));
    }

    private JsonObject expectedJsonObject() {
        final JsonArray array = getJsonBuilderFactory().createArrayBuilder()
                .add("Attribute 1")
                .add("Attribute 2").build();

        return getJsonBuilderFactory().createObjectBuilder()
                .add("id", ID.toString())
                .add("name", NAME)
                .add("attributes", array).build();
    }

    public static class Pojo {

        private final UUID id;
        private final String name;
        private final List<String> attributes;

        public Pojo(final UUID id, final String name, final List<String> attributes) {
            this.id = id;
            this.name = name;
            this.attributes = attributes;
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
}
