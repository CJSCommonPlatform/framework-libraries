package uk.gov.justice.services.metrics;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimerRegistrarTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Test
    void shouldRegisterTimerWithGivenPercentiles() {
        final Timer.Builder timerBuilder = mock(Timer.Builder.class);

        try (var timer = mockStatic(Timer.class)) {
            timer.when(() -> Timer.builder(anyString())).thenReturn(timerBuilder);
            when(timerBuilder.publishPercentiles(any(double[].class))).thenReturn(timerBuilder);
            when(timerBuilder.register(any(MeterRegistry.class))).thenReturn(mock(Timer.class));

            final TimerRegistrar timerRegistrar = new TimerRegistrar(meterRegistry);
            timerRegistrar.registerTimer("testTimer", 0.5, 0.95);

            verify(timerBuilder).publishPercentiles(0.5, 0.95);
            verify(timerBuilder).register(meterRegistry);
        }
    }
}