package uk.gov.justice.services.metrics;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;

class PrometheusMeterRegistryProducerTest {

    @Test
    void shouldCreateAndReturnPrometheusRegistry() {
        final PrometheusMeterRegistryProducer prometheusMeterRegistryProducer = new PrometheusMeterRegistryProducer();

        final MeterRegistry meterRegistry = prometheusMeterRegistryProducer.createMeterRegistry();

        assertInstanceOf(PrometheusMeterRegistry.class, meterRegistry);
    }

    @Test
    void shouldReturnSameRegistryForMultipleBeanInvocations() {
        final PrometheusMeterRegistryProducer prometheusMeterRegistryProducer = new PrometheusMeterRegistryProducer();

        final MeterRegistry meterRegistry1 = prometheusMeterRegistryProducer.createMeterRegistry();
        final MeterRegistry meterRegistry2 = prometheusMeterRegistryProducer.createMeterRegistry();

        assertSame(meterRegistry1, meterRegistry2);
    }
}