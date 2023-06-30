package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.ADDITIONAL_PROPERTIES_NAME;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdditionalPropertiesSerializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private BeanSerializerBase dummySerializer = BeanSerializer.createDummy(objectMapper.constructType(TestPerson.class));

    private AdditionalPropertiesSerializer additionalPropertiesSerializer;

    @Mock
    private JsonGenerator jsonGeneratorMock;

    @Mock
    private SerializerProvider serializerProviderMock;

    @BeforeEach
    public void setup() {
        additionalPropertiesSerializer = new AdditionalPropertiesSerializer(
                dummySerializer,
                newHashSet(ADDITIONAL_PROPERTIES_NAME));

        when(serializerProviderMock.mappingException(anyString(), any(Object.class))).thenReturn(new JsonMappingException(null, ""));
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
    public void shouldNotInvokeJsonGeneratorWriteObjectFieldWhenAdditionalPropertiesIsNull() throws Exception {

        final TestPerson person = new TestPerson("TEST", "PERSON", 21, null);

        additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(1)).writeStartObject();
        verify(jsonGeneratorMock, times(1)).writeEndObject();
    }

    @Test
    public void shouldNotInvokeJsonGeneratorWriteObjectFieldWhenProvidedObjectWithEmptyAdditionalProperties() throws Exception {

        final Map<String, Object> additionalPropertiesMap = new HashMap<>();

        final TestPerson person = new TestPerson("TEST", "PERSON", 21, additionalPropertiesMap);

        additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(1)).writeStartObject();
        verify(jsonGeneratorMock, times(0)).writeObjectField(anyString(), any(Object.class));
        verify(jsonGeneratorMock, times(1)).writeEndObject();
    }

    @Test
    public void shouldThrowExceptionThroughSerializerProviderWhenJsonGeneratorThrowsIOException() throws IOException {

        final Map<String, Object> additionalProperties = Map.of("name", "value");

        final TestWithAdditionalProperties person = new TestWithAdditionalProperties("TEST", "PERSON", 21, additionalProperties);

        doThrow(new IOException()).when(jsonGeneratorMock).writeObjectField(anyString(), any(Object.class));

        try {
            additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);
            fail();
        } catch (final IOException expected) {}

        verify(serializerProviderMock, times(1)).mappingException(anyString(), any(Object.class));

    }

    @Test
    public void shouldThrowExceptionThroughSerializerProviderWhenNoAdditionalPropertiesAttr() {

        final TestWithNoAdditionalProperties person = new TestWithNoAdditionalProperties("TEST", "PERSON", 21);

        try {
            additionalPropertiesSerializer.serialize(person, jsonGeneratorMock, serializerProviderMock);
        } catch (final IOException expected) {
            // Expected
        }

        verify(serializerProviderMock, times(1)).mappingException(anyString(), any(Object.class));
    }

    @Test
    public void shouldReturnUnsupportedOperationExceptionWhenCallingWithObjectIdWriter() {
        assertThrows(UnsupportedOperationException.class, () -> additionalPropertiesSerializer.withObjectIdWriter(null));
    }

    @Test
    public void shouldThrowUnsupportedOperationExceptionWhenCallingWithIgnorals() {
        assertThrows(UnsupportedOperationException.class, () -> additionalPropertiesSerializer.withIgnorals(new HashSet<>()));
    }

    @Test
    public void shouldThrowUnsupportedOperationExceptionWhenCallingAsArraySerializer() {
        assertThrows(UnsupportedOperationException.class, () -> additionalPropertiesSerializer.asArraySerializer());
    }

    @Test
    public void shouldThrowUnsupportedOperationExceptionWhenCallingWithFilterId() {
        assertThrows(UnsupportedOperationException.class, () -> additionalPropertiesSerializer.withFilterId(new Object()));
    }

    @Test
    public void shouldThrowUnsupportedOperationExceptionWhenCallingWithByNameInclusion() {
        assertThrows(UnsupportedOperationException.class, () -> additionalPropertiesSerializer.withByNameInclusion(newHashSet("stuff"), newHashSet("other stuff")));
    }

    @Test
    public void shouldThrowUnsupportedOperationExceptionWhenCallingWithProperties() {
        assertThrows(UnsupportedOperationException.class, () -> additionalPropertiesSerializer.withProperties(new BeanPropertyWriter[0], new BeanPropertyWriter[0]));
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

    private static class TestWithAdditionalProperties {

        private String firstName;
        private String surname;
        private int age;
        private Map<String, Object> additionalProperties;

        public TestWithAdditionalProperties(final String firstName, final String surname, final int age, Map<String, Object> additionalProperties) {
            this.firstName = firstName;
            this.surname = surname;
            this.age = age;
            this.additionalProperties = additionalProperties;
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
