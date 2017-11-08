package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static com.google.common.base.CharMatcher.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.ADDITIONAL_PROPERTIES_NAME;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdditionalPropertiesSerializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private BeanSerializerBase dummySerializer = BeanSerializer.createDummy(objectMapper.constructType(TestPerson.class));

    private AdditionalPropertiesSerializer additionalPropertiesSerializer;

    @Mock
    private JsonGenerator jsonGeneratorMock;

    @Mock
    private SerializerProvider serializerProviderMock;

    @Before
    public void setup() {
        additionalPropertiesSerializer
                = new AdditionalPropertiesSerializer(dummySerializer,
                new String[]{ADDITIONAL_PROPERTIES_NAME});

        when(serializerProviderMock.mappingException(anyString(), anyObject())).thenReturn(new JsonMappingException(""));
    }

    @Test
    public void shouldInvokeJsonGeneratorWriteObjectFieldWhenProvidedObjectWithAdditionalProperties() throws Exception {

        final String propertyOneKey = "TEST ADDITIONAL PROPERTY ONE KEY";
        final String propertyOneValue = "TEST ADDITIONAL PROPERTY ONE VALUE";
        final String propertyTwoKey = "TEST ADDITIONAL PROPERTY TWO KEY";
        final int propertyTwoValue = 101;

        final Map<String, Object> additionalPropertiesMap = new HashMap<>();
        additionalPropertiesMap.put(propertyOneKey, propertyOneValue);
        additionalPropertiesMap.put(propertyTwoKey, propertyTwoValue);

        final TestPerson person = new TestPerson("TEST", "PERSON", 21, additionalPropertiesMap);

        additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(1)).writeStartObject();
        verify(jsonGeneratorMock, times(1)).writeObjectField(propertyOneKey, propertyOneValue);
        verify(jsonGeneratorMock, times(1)).writeObjectField(propertyTwoKey, propertyTwoValue);
        verify(jsonGeneratorMock, times(1)).writeEndObject();
    }

    @Test
    public void shouldNotInvokeJsonGeneratorWriteObjectFieldWhenProvidedObjectWithEmptyAdditionalProperties() throws Exception {

        final Map<String, Object> additionalPropertiesMap = new HashMap<>();

        final TestPerson person = new TestPerson("TEST", "PERSON", 21, additionalPropertiesMap);

        additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(1)).writeStartObject();
        verify(jsonGeneratorMock, times(0)).writeObjectField(anyString(), any());
        verify(jsonGeneratorMock, times(1)).writeEndObject();
    }

    @Test
    public void shouldThrowExceptionThroughSerializerProviderWhenJsonGeneratorThrowsIOException() throws IOException {

        final TestWithNoAdditionalProperties person = new TestWithNoAdditionalProperties("TEST", "PERSON", 21);

        doThrow(new IOException()).when(jsonGeneratorMock).writeObjectField(anyString(), any());

        try {
            additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);
        } catch (final IOException ioex) {
            // Expected
        }

        verify(serializerProviderMock, times(1)).mappingException(anyString(), anyObject());

    }

    @Test
    public void shouldThrowExceptionThroughSerializerProviderWhenNoAdditionalPropertiesAttr() {

        final TestWithNoAdditionalProperties person = new TestWithNoAdditionalProperties("TEST", "PERSON", 21);

        try {
            additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);
        } catch (final IOException ioex) {
            // Expected
        }

        verify(serializerProviderMock, times(1)).mappingException(anyString(), anyObject());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnUnsupportedOperationExceptionFromWithObjectIdWriter() {
        additionalPropertiesSerializer.withObjectIdWriter(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnUnsupportedOperationExceptionFromWithIgnorals() {
        additionalPropertiesSerializer.withIgnorals(new String[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnUnsupportedOperationExceptionFromAsArraySerializer() {
        additionalPropertiesSerializer.asArraySerializer();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnUnsupportedOperationExceptionFromWithFilterId() {
        additionalPropertiesSerializer.withFilterId(new Object());
    }



    private static class TestWithNoAdditionalProperties {

        private String firstName;
        private String surname;
        private int age;

        public TestWithNoAdditionalProperties(final String firstName, final String surname, final int age) {
            this.firstName = firstName;
            this.surname = surname;
            this.age = age;
        }
    }


    private static class TestPerson {

        private String firstName;
        private String surname;
        private int age;
        private Map<String, Object> additionalProperties;


        public TestPerson(final String firstName, final String surname, final int age, Map<String, Object> additionalProperties) {
            this.firstName = firstName;
            this.surname = surname;
            this.age = age;
            this.additionalProperties = additionalProperties;
        }
    }
}
