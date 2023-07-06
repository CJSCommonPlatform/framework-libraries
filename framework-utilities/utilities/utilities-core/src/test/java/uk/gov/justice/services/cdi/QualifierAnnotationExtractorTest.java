package uk.gov.justice.services.cdi;

import static com.google.common.collect.ImmutableSet.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QualifierAnnotationExtractorTest {

    @InjectMocks
    private QualifierAnnotationExtractor qualifierAnnotationExtractor;

    @Test
    public void shouldGetTheSubscriptionNameQualifierFromAnEventListenerClass() throws Exception {

        final InjectionPoint injectionPoint = mock(InjectionPoint.class);
        final Annotation annotation = MayAnnotatedClass.class.getDeclaredAnnotation(Named.class);

        when(injectionPoint.getQualifiers()).thenReturn(of(annotation));

        final Named named = qualifierAnnotationExtractor.getFrom(injectionPoint, Named.class);

        assertThat(named.value(), is("Dummy"));
    }

    @Test
    public void shouldThrowARuntimeExceptionIfNoSubscriptionNameQualifierOnAnEventListener() throws Exception {

        final InjectionPoint injectionPoint = mock(InjectionPoint.class);
        final Bean bean = mock(Bean.class);

        final Set<Annotation> emptySet = new HashSet<>();

        when(injectionPoint.getQualifiers()).thenReturn(emptySet);
        when(injectionPoint.getBean()).thenReturn(bean);
        when(bean.getName()).thenReturn(MayAnnotatedClass.class.getName());

        try {
            qualifierAnnotationExtractor.getFrom(injectionPoint, Named.class);
            fail();
        } catch (final InjectionException expected) {
            assertThat(expected.getMessage(), is("Failed to find 'javax.inject.Named' annotation on bean 'uk.gov.justice.services.cdi.QualifierAnnotationExtractorTest$MayAnnotatedClass'"));
        }
    }

    @Named("Dummy")
    public static class MayAnnotatedClass {

        public void somethingHappened() {

        }
    }
}

