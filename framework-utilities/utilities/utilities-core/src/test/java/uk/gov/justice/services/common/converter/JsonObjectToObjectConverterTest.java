package uk.gov.justice.services.common.converter;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.doThrow;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JsonObjectToObjectConverterTest {

    private static final UUID ID = randomUUID();
    private static final UUID INTERNAL_ID = randomUUID();
    private static final String NAME = "Pojo";
    private static final String ATTRIBUTE_1 = "Attribute 1";
    private static final String ATTRIBUTE_2 = "Attribute 2";
    private static final String INTERNAL_NAME = "internalName";

    @Spy
    private ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    @InjectMocks
    private JsonObjectToObjectConverter jsonObjectToObjectConverter;

    @Test
    public void shouldConvertPojoToJsonObject() throws Exception {

        final JsonObject jsonObject = jsonObject();
        final Pojo pojo = jsonObjectToObjectConverter.convert(jsonObject, Pojo.class);

        assertThat(pojo, notNullValue());
        assertThat(pojo.getId(), equalTo(ID));
        assertThat(pojo.getName(), equalTo(NAME));
        assertThat(pojo.getAttributes(), hasItems(ATTRIBUTE_1, ATTRIBUTE_2));
        assertThat(pojo.getInternalPojo().getInternalId(), equalTo(INTERNAL_ID));
        assertThat(pojo.getInternalPojo().getInternalName(), equalTo(INTERNAL_NAME));
    }

    @Test
    public void shouldConvertToPojoWithUTCDateTime() throws Exception {

        assertThat(jsonObjectToObjectConverter
                        .convert(getJsonBuilderFactory().createObjectBuilder().add("dateTime", "2016-07-25T13:09:01.0+00:00").build(),
                                PojoWithDateTime.class).getDateTime(),
                equalTo(ZonedDateTime.of(2016, 7, 25, 13, 9, 1, 0, ZoneId.of("UTC"))));
        assertThat(jsonObjectToObjectConverter
                        .convert(getJsonBuilderFactory().createObjectBuilder().add("dateTime", "2016-07-25T13:09:01.0Z").build(),
                                PojoWithDateTime.class).getDateTime(),
                equalTo(ZonedDateTime.of(2016, 7, 25, 13, 9, 1, 0, ZoneId.of("UTC"))));
        assertThat(jsonObjectToObjectConverter
                        .convert(getJsonBuilderFactory().createObjectBuilder().add("dateTime", "2016-07-25T13:09:01Z").build(),
                                PojoWithDateTime.class).getDateTime(),
                equalTo(ZonedDateTime.of(2016, 7, 25, 13, 9, 1, 0, ZoneId.of("UTC"))));
        assertThat(jsonObjectToObjectConverter
                        .convert(getJsonBuilderFactory().createObjectBuilder().add("dateTime", "2016-07-25T16:09:01.0+03:00").build(),
                                PojoWithDateTime.class).getDateTime(),
                equalTo(ZonedDateTime.of(2016, 7, 25, 13, 9, 1, 0, ZoneId.of("UTC"))));

    }

    @Test
    public void shouldThrowExceptionOnConversionError() throws IOException {

        final UUID uuid = randomUUID();

        final JsonObject jsonObject = getJsonBuilderFactory().createObjectBuilder().add("id", uuid.toString()).build();

        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(jsonObject);

        try {
            jsonObjectToObjectConverter.convert(jsonObject, Pojo.class);
        } catch (final IllegalArgumentException expected) {
            assertThat(expected.getMessage(), is("Error while converting to uk.gov.justice.services.common.converter.JsonObjectToObjectConverterTest$Pojo from json (obfuscated):[{\"id\":\"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\"}]"));
            assertThat(expected.getCause(), is(nullValue()));
        }
    }

    @Test
    public void shouldConvertJsonObjectToSingleArgumentConstructorPojo() throws Exception {

        final UUID id = randomUUID();
        final JsonObject payloadAsJsonObject = getJsonBuilderFactory().createObjectBuilder()
                .add("id", id.toString())
                .build();

        final SingleArgumentConstructorPojo pojo = jsonObjectToObjectConverter.convert(payloadAsJsonObject, SingleArgumentConstructorPojo.class);

        assertThat(pojo, is(notNullValue()));
        assertThat(pojo.getId(), is(id));
    }

    private JsonObject jsonObject() {
        final JsonArray array = getJsonBuilderFactory().createArrayBuilder()
                .add(ATTRIBUTE_1)
                .add(ATTRIBUTE_2).build();

        return getJsonBuilderFactory().createObjectBuilder()
                .add("id", ID.toString())
                .add("name", NAME)
                .add("internalPojo", getJsonBuilderFactory().createObjectBuilder()
                        .add("internalId", INTERNAL_ID.toString())
                        .add("internalName", INTERNAL_NAME).build())
                .add("attributes", array).build();
    }

    public static class Pojo {

        private UUID id;
        private String name;
        private List<String> attributes;
        private InternalPojo internalPojo;

        public Pojo () {

        }

        public Pojo(final UUID id, final String name, final List<String> attributes,
                    final InternalPojo internalPojo) {
            this.id = id;
            this.name = name;
            this.attributes = attributes;
            this.internalPojo = internalPojo;
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

        public InternalPojo getInternalPojo() {
            return internalPojo;
        }
    }

    public static class InternalPojo {

        private UUID internalId;
        private String internalName;

        public InternalPojo() {

        }

        public InternalPojo(final UUID internalId, final String internalName) {
            this.internalId = internalId;
            this.internalName = internalName;
        }

        public UUID getInternalId() {
            return internalId;
        }

        public String getInternalName() {
            return internalName;
        }
    }

    public static class PojoWithDateTime {

        private final ZonedDateTime dateTime;

        public PojoWithDateTime(@JsonProperty("dateTime") final ZonedDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public ZonedDateTime getDateTime() {
            return dateTime;
        }
    }

    public static class SingleArgumentConstructorPojo {
        private UUID id;

        public SingleArgumentConstructorPojo (UUID id) {
            this.id = id;
        }

        public UUID getId() {
            return id;
        }
    }

}
