package uk.gov.justice.schema.catalog.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.spi.InjectionPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(MockitoJUnitRunner.class)
public class LoggerProducerTest {

    @InjectMocks
    private LoggerProducer loggerProducer;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCreateALoggerWithTheCorrectCallingClass() throws Exception {

        final Class callingClass = UriResolver.class;
        final InjectionPoint injectionPoint = mock(InjectionPoint.class, RETURNS_DEEP_STUBS);

        when(injectionPoint.getMember().getDeclaringClass()).thenReturn(callingClass);

        final Logger logger = loggerProducer.loggerProducer(injectionPoint);

        assertThat(logger.getName(), is(LoggerFactory.getLogger(callingClass).getName()));
    }
}
