package uk.gov.justice.services.healthcheck.api;

import static java.util.Collections.emptyList;

import java.util.List;

public class DefaultIgnoredHealthcheckNamesProvider implements IgnoredHealthcheckNamesProvider {

    @Override
    public List<String> getNamesOfIgnoredHealthChecks() {
        return emptyList();
    }
}
