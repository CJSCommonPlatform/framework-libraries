package uk.gov.justice.services.metrics;

import io.micrometer.core.instrument.MeterRegistry;

public interface MeterRegistryProducer {
    MeterRegistry createMeterRegistry();
}
