package uk.gov.justice.services.metrics;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Dependent
public class TimerRegistrar {

    private final MeterRegistry meterRegistry;

    @Inject
    public TimerRegistrar(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void registerTimer(String timerName, double... percentiles) {
        Timer.builder(timerName)
                .publishPercentiles(percentiles)
                .register(meterRegistry);
    }
}
