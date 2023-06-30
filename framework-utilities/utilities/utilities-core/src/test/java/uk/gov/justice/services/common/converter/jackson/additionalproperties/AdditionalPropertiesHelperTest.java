package uk.gov.justice.services.common.converter.jackson.additionalproperties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesHelper.hasAdditionalPropertiesName;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.junit.jupiter.api.Test;

public class AdditionalPropertiesHelperTest {


    @Test
    public void shouldReturnTrueFromPredicateWhenAdditionalPropertiesInList() {

        final PropertyName propertyNameMock = mock(PropertyName.class);
        final BeanPropertyDefinition bpdMock = mock(BeanPropertyDefinition.class);

        when(propertyNameMock.getSimpleName()).thenReturn("additionalProperties");
        when(bpdMock.getFullName()).thenReturn(propertyNameMock);

        assertTrue(hasAdditionalPropertiesName.test(bpdMock));

    }

    @Test
    public void shouldReturnFalseFromPredicateWhenAdditionalPropertiesNotInList() {

        final PropertyName propertyNameMock = mock(PropertyName.class);
        final BeanPropertyDefinition bpdMock = mock(BeanPropertyDefinition.class);

        when(propertyNameMock.getSimpleName()).thenReturn("anotherName");
        when(bpdMock.getFullName()).thenReturn(propertyNameMock);

        assertFalse(hasAdditionalPropertiesName.test(bpdMock));

    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<AdditionalPropertiesHelper> constructor = AdditionalPropertiesHelper.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
