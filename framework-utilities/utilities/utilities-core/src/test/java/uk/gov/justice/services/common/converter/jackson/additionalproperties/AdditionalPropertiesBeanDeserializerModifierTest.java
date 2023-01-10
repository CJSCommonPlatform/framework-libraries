package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AdditionalPropertiesBeanDeserializerModifierTest {

    private BeanDeserializerBase beanDeserializerBase;
    private BeanDescription beanDesc;
    private POJOPropertyBuilder pojoPropertyBuilder;

    @Before
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
