package uk.gov.justice.services.cdi;

import static com.google.common.collect.ImmutableSet.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

        final InjectionPoint injectionPoint = mock(InjectionPoint.class, RETURNS_DEEP_STUBS.get());

        final Set<Annotation> emptySet = new HashSet<>();

        when(injectionPoint.getQualifiers()).thenReturn(emptySet);
        when(injectionPoint.getBean().getName()).thenReturn(MayAnnotatedClass.class.getName());

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

