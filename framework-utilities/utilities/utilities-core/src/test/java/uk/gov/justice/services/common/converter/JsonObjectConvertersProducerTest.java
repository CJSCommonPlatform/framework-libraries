package uk.gov.justice.services.common.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JsonObjectConvertersProducerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonObjectConvertersProducer jsonObjectConvertersProducer;

    @Test
    public void shouldCreateANewJsonObjectToObjectConverter() throws Exception {

        final JsonObjectToObjectConverter jsonObjectToObjectConverter = jsonObjectConvertersProducer
                .jsonObjectToObjectConverter();

        assertThat(getValueOfField(jsonObjectToObjectConverter, "objectMapper", ObjectMapper.class), is(objectMapper));
    }

    @Test
    public void shouldCreateAnObjectToJsonObjectConverter() throws Exception {

        final ObjectToJsonObjectConverter objectToJsonObjectConverter = jsonObjectConvertersProducer
                .objectToJsonObjectConverter();

        assertThat(getValueOfField(objectToJsonObjectConverter, "mapper", ObjectMapper.class), is(objectMapper));
    }

    @Test
    public void shouldCreateAnObjectToJsonValueConverter() throws Exception {

        final ObjectToJsonValueConverter objectToJsonValueConverter = jsonObjectConvertersProducer
                .objectToJsonValueConverter();

        assertThat(getValueOfField(objectToJsonValueConverter, "mapper", ObjectMapper.class), is(objectMapper));
    }
}