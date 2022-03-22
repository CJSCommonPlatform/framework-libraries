package uk.gov.justice.services.healthcheck.api;

public interface Healthcheck {

    String getHealthcheckName();
    String healthcheckDescription();
    HealthcheckResult runHealthcheck();
}
