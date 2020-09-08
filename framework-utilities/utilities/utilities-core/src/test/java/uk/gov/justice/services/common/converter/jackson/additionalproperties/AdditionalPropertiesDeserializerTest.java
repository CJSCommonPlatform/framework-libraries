package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdditionalPropertiesDeserializerTest {

    @Mock
    private BeanDeserializerBase beanDeserializerBase;

    @InjectMocks
    private AdditionalPropertiesDeserializer beanDeserializer;

    @Test(expected = Exception.class)
    public void shouldReturnAdditionalPropertiesDeserializerException() throws IOException {
        final Map<Integer, Object> invalidAdditionalProperties = Collections.singletonMap(1, new Object());
        final Object personWithInvalidAdditionalProperties = new PersonWithWrongAdditionalProperties("Adam", 34, invalidAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_STRING);
        when(jp.getCurrentName()).thenReturn("Test");
        when(jp.getValueAsString()).thenReturn("Test Value");

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithInvalidAdditionalProperties, "additionalProperties");
    }

    @Test
    public void shouldReturnAdditionalPropertiesDeserializer() throws IOException {
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put("Test", "Test Value");

        final Object personWithAdditionalProperties = new PersonWithAdditionalProperties(
                "Adam",
                34,
                validAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_STRING);
        when(jp.getCurrentName()).thenReturn("Test");
        when(jp.getValueAsString()).thenReturn("Test Value");

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");
    }

    @Test
    public void shouldHandleStringProperties() throws Exception {

        final String propertyName = "aString";
        final String propertyValue = "fred";
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put(propertyName, propertyValue);

        final PersonWithAdditionalProperties personWithAdditionalProperties = new PersonWithAdditionalProperties(
                "Adam",
                34,
                validAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_STRING);
        when(jp.getCurrentName()).thenReturn(propertyName);
        when(jp.getValueAsString()).thenReturn(propertyValue);

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");

        assertThat(personWithAdditionalProperties.getAdditionalProperties().get(propertyName), is(propertyValue));
    }

    @Test
    public void shouldHandleIntProperties() throws Exception {

        final String propertyName = "anInt";
        final int propertyValue = 23;
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put(propertyName, propertyValue);

        final PersonWithAdditionalProperties personWithAdditionalProperties = new PersonWithAdditionalProperties(
                "Adam",
                34,
                validAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_NUMBER_INT);
        when(jp.getCurrentName()).thenReturn(propertyName);
        when(jp.getValueAsInt()).thenReturn(propertyValue);

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");

        assertThat(personWithAdditionalProperties.getAdditionalProperties().get(propertyName), is(propertyValue));
    }

    @Test
    public void shouldHandleFloatProperties() throws Exception {

        final String propertyName = "aFloat";
        final float propertyValue = 23.23F;
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put(propertyName, propertyValue);

        final PersonWithAdditionalProperties personWithAdditionalProperties = new PersonWithAdditionalProperties(
                "Adam",
                34,
                validAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_NUMBER_FLOAT);
        when(jp.getCurrentName()).thenReturn(propertyName);
        when(jp.getValueAsDouble()).thenReturn((double) propertyValue);

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");

        assertThat(personWithAdditionalProperties.getAdditionalProperties().get(propertyName), is(propertyValue));
    }

    @Test
    public void shouldHandleBooleanTrueProperties() throws Exception {

        final String propertyName = "aTrueBoolean";
        final boolean propertyValue = true;
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put(propertyName, propertyValue);

        final PersonWithAdditionalProperties personWithAdditionalProperties = new PersonWithAdditionalProperties(
                "Adam",
                34,
                validAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_TRUE);
        when(jp.getCurrentName()).thenReturn(propertyName);
        when(jp.getValueAsBoolean()).thenReturn(propertyValue);

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");

        assertThat(personWithAdditionalProperties.getAdditionalProperties().get(propertyName), is(propertyValue));
    }

    @Test
    public void shouldHandleBooleanFalseProperties() throws Exception {

        final String propertyName = "aFalseBoolean";
        final boolean propertyValue = false;
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put(propertyName, propertyValue);

        final PersonWithAdditionalProperties personWithAdditionalProperties = new PersonWithAdditionalProperties(
                "Adam",
                34,
                validAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_FALSE);
        when(jp.getCurrentName()).thenReturn(propertyName);
        when(jp.getValueAsBoolean()).thenReturn(propertyValue);

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");

        assertThat(personWithAdditionalProperties.getAdditionalProperties().get(propertyName), is(propertyValue));
    }

    @Test
    public void shouldHandleNullProperties() throws Exception {

        final String propertyName = "aNullValue";
        final Object propertyValue = null;
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put(propertyName, propertyValue);

        final PersonWithAdditionalProperties personWithAdditionalProperties = new PersonWithAdditionalProperties(
                "Adam",
                34,
                validAdditionalProperties);

        final DeserializationContext ctxt = mock(DeserializationContext.class);
        final JsonParser jp = mock(JsonParser.class);

        when(jp.getCurrentToken()).thenReturn(VALUE_NULL);
        when(jp.getCurrentName()).thenReturn(propertyName);

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");

        assertThat(personWithAdditionalProperties.getAdditionalProperties().get(propertyName), is(propertyValue));
    }

    public static class PersonWithWrongAdditionalProperties {

        private String name;
        private int age;
        private Map<Integer, Object> additionalProperties;

        public PersonWithWrongAdditionalProperties(final String name, final int age, final Map<Integer, Object> invalidAdditionalProperties) {
            this.name = name;
            this.age = age;
            this.additionalProperties = invalidAdditionalProperties;
        }
    }

    public static class PersonWithAdditionalProperties {

        private final String name;
        private final int age;
        private final Map<String, Object> additionalProperties;

        public PersonWithAdditionalProperties(final String name, final int age, final Map<String, Object> additionalProperties) {
            this.name = name;
            this.age = age;
            this.additionalProperties = additionalProperties;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }
    }
}
