package uk.gov.justice.services.cdi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.util.Clock;

import javax.enterprise.inject.spi.InjectionPoint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ExtendWith(MockitoExtension.class)
public class LoggerProducerTest {

    @InjectMocks
    private LoggerProducer loggerProducer;

    @Test @SuppressWarnings("unchecked")
    public void shouldCreateALoggerWithTheCorrectCallingClass() throws Exception {

        final Class callingClass = Clock.class;
        final InjectionPoint injectionPoint = mock(InjectionPoint.class, RETURNS_DEEP_STUBS);

        when(injectionPoint.getMember().getDeclaringClass()).thenReturn(callingClass);

        final Logger logger = loggerProducer.loggerProducer(injectionPoint);

        assertThat(logger.getName(), is(LoggerFactory.getLogger(callingClass).getName()));
    }
}
