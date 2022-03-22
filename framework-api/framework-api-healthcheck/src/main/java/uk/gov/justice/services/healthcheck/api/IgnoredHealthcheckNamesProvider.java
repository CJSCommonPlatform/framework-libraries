package uk.gov.justice.services.healthcheck.api;

import java.util.List;

public interface IgnoredHealthcheckNamesProvider {

    List<String> getNamesOfIgnoredHealthChecks();
}
