package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class AdditionalPropertiesBeanDeserializerModifierTest {

    private BeanDeserializerBase beanDeserializerBase;
    private BeanDescription beanDesc;
    private POJOPropertyBuilder pojoPropertyBuilder;

    @BeforeEach
    public void setUp() {
        beanDeserializerBase = mock(BeanDeserializerBase.class);
        beanDesc = mock(BeanDescription.class);
        pojoPropertyBuilder = mock(POJOPropertyBuilder.class);
        when(beanDesc.findProperties()).thenReturn(Arrays.asList(pojoPropertyBuilder));
    }

    @Test
    public void shouldReturnAdditionalPropertiesDeserializer() {
        when(pojoPropertyBuilder.getFullName()).thenReturn(new PropertyName("additionalProperties"));
        final JsonDeserializer jsonDeserializer = new AdditionalPropertiesBeanDeserializerModifier().modifyDeserializer(null, beanDesc, beanDeserializerBase);
        assertNotNull(jsonDeserializer);
        assertTrue(jsonDeserializer instanceof AdditionalPropertiesDeserializer);
    }

    @Test
    public void shouldNotReturnAdditionalPropertiesDeserializer() {
        when(pojoPropertyBuilder.getFullName()).thenReturn(new PropertyName("invalidAdditionalProperties"));
        final JsonDeserializer jsonDeserializer = new AdditionalPropertiesBeanDeserializerModifier().modifyDeserializer(null, beanDesc, beanDeserializerBase);
        assertNotNull(jsonDeserializer);
        assertFalse(jsonDeserializer instanceof AdditionalPropertiesDeserializer);
    }
}
