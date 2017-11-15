package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdditionalPropertiesDeserializerTest {

    private BeanDeserializerBase beanDeserializerBase;
    private DeserializationContext ctxt;
    private JsonParser jp;

    @Before
    public void setUp() throws IOException {
        beanDeserializerBase = mock(BeanDeserializerBase.class);
        ctxt = mock(DeserializationContext.class);
        jp = mock(JsonParser.class);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getCurrentName()).thenReturn("Test");
        when(jp.getValueAsString()).thenReturn("Test Value");
    }

    @Test(expected = Exception.class)
    public void shouldReturnAdditionalPropertiesDeserializerException() throws IOException {
        final Map<Integer, Object> invalidAdditionalProperties = Collections.singletonMap(1, new Object());
        final Object personWithInvalidAdditionalProperties = new PersonWithWrongAdditionalProperties("Adam", 34, invalidAdditionalProperties);
        final AdditionalPropertiesDeserializer beanDeserializer = new AdditionalPropertiesDeserializer(beanDeserializerBase);

        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithInvalidAdditionalProperties, "additionalProperties");
    }

    @Test
    public void shouldReturnAdditionalPropertiesDeserializer() throws IOException {
        final Map<String, Object> validAdditionalProperties = new HashMap<>();
        validAdditionalProperties.put("Test", "Test Value");
        final Object personWithAdditionalProperties = new PersonWithAdditionalProperties("Adam", 34, validAdditionalProperties);
        final AdditionalPropertiesDeserializer beanDeserializer = new AdditionalPropertiesDeserializer(beanDeserializerBase);
        beanDeserializer.handleUnknownProperty(jp, ctxt, personWithAdditionalProperties, "additionalProperties");
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

        private String name;
        private int age;
        private Map<String, Object> additionalProperties;

        public PersonWithAdditionalProperties(final String name, final int age, final Map<String, Object> additionalProperties) {
            this.name = name;
            this.age = age;
            this.additionalProperties = additionalProperties;
        }
    }
}
