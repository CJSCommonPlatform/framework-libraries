package uk.gov.justice.services.common.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GlobalValueProducerTest {

    @InjectMocks
    private GlobalValueProducer valueProducer;

    @Mock
    private InjectionPoint propertyInjectionPoint;

    @Mock
    private Annotated annotated;

    @Mock
    private GlobalValue annotation;

    @Mock
    private InitialContext initialContext;

    @BeforeEach
    public void setup() throws NamingException {
        when(propertyInjectionPoint.getAnnotated()).thenReturn(annotated);
        when(annotated.getAnnotation(GlobalValue.class)).thenReturn(annotation);
    }

    @Test
    public void shouldReturnStringPropertyValue() throws NamingException {
        when(initialContext.lookup("java:global/myProperty")).thenReturn("some value");
        when(annotation.key()).thenReturn("myProperty");

        assertThat(valueProducer.stringValueOf(propertyInjectionPoint), equalTo("some value"));
    }

    @Test
    public void shouldReturnLongPropertyValue() throws NamingException {
        when(initialContext.lookup("java:global/myLongProperty")).thenReturn("100");
        when(annotation.key()).thenReturn("myLongProperty");
        assertThat(valueProducer.longValueOf(propertyInjectionPoint), equalTo(100L));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnDefaultValueWhenNotFound() throws NamingException {
        when(initialContext.lookup("java:global/myOtherProperty")).thenThrow(NameNotFoundException.class);
        when(annotation.key()).thenReturn("myOtherProperty");
        when(annotation.defaultValue()).thenReturn("some default value");

        assertThat(valueProducer.stringValueOf(propertyInjectionPoint), equalTo("some default value"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldThrowExceptionWhenNotFoundAndNoDefaultValue() throws NamingException {
        when(initialContext.lookup("java:global/unknownProperty")).thenThrow(NameNotFoundException.class);
        when(annotation.key()).thenReturn("unknownProperty");

        when(annotation.defaultValue()).thenReturn(CommonValueAnnotationDef.NULL_DEFAULT);

        assertThrows(MissingPropertyException.class, () ->valueProducer.stringValueOf(propertyInjectionPoint));
    }
}