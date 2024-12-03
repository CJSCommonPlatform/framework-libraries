package uk.gov.justice.services.metrics;

import static io.micrometer.prometheusmetrics.PrometheusConfig.DEFAULT;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

@ApplicationScoped
public class PrometheusMeterRegistryProducer implements MeterRegistryProducer {

    private final PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(DEFAULT);

    @Produces
    public PrometheusMeterRegistry createMeterRegistry() {
        return meterRegistry;
    }
}