package uk.gov.justice.services.messaging.logging;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class LoggerUtilsTest {

    @Mock
    Logger logger;
    
    @Test
    public void shouldDoNothing() throws Exception {
        when(logger.isTraceEnabled()).thenReturn(false);
        LoggerUtils.trace(logger, () -> "test");
        verify(logger).isTraceEnabled();
    }

    @Test
    public void shouldLogTrace() throws Exception {
        when(logger.isTraceEnabled()).thenReturn(true);
        LoggerUtils.trace(logger, () -> "test");
        verify(logger).trace("test");
    }

    @Test
    public void shouldLogErrorIfExceptionThrown() throws Exception {
        final RuntimeException exception = new RuntimeException();
        when(logger.isTraceEnabled()).thenReturn(true);
        doThrow(exception).when(logger).trace("test");

        LoggerUtils.trace(logger, () -> "test");
        verify(logger).error("Could not generate trace log message", exception);
    }

}
