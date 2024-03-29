package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class AdditionalPropertiesBeanSerializerModifierTest {

    private BeanDescription beanDesc;
    private POJOPropertyBuilder pojoPropertyBuilder;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        beanDesc = mock(BeanDescription.class);
        pojoPropertyBuilder = mock(POJOPropertyBuilder.class);
        when(beanDesc.findProperties()).thenReturn(Arrays.asList(pojoPropertyBuilder));
    }


    @Test
    public void shouldReturnAdditionalPropertiesSerializerWhenBeanDescriptionContainsAdditionalProperties() {

        when(pojoPropertyBuilder.getFullName()).thenReturn(new PropertyName("additionalProperties"));

        final BeanSerializerBase serializer = BeanSerializer.createDummy(objectMapper.constructType(String.class));
        final JsonSerializer result = new AdditionalPropertiesBeanSerializerModifier().modifySerializer(null, beanDesc, serializer);

        assertNotNull(result);
        assertTrue(result instanceof AdditionalPropertiesSerializer);
    }

    @Test
    public void shouldNotReturnAdditionalPropertiesSerializerWhenBeanDescriptionContainsAdditionalProperties() {

        when(pojoPropertyBuilder.getFullName()).thenReturn(new PropertyName("invalidAdditionalProperties"));

        final BeanSerializerBase serializer = BeanSerializer.createDummy(objectMapper.constructType(String.class));
        final JsonSerializer result = new AdditionalPropertiesBeanSerializerModifier().modifySerializer(null, beanDesc, serializer);

        assertNotNull(result);
        assertFalse(result instanceof AdditionalPropertiesSerializer);
    }
}
