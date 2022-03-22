package uk.gov.justice.services.healthcheck.api;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.healthcheck.api.HealthcheckResult.failure;
import static uk.gov.justice.services.healthcheck.api.HealthcheckResult.success;

import org.junit.Test;

public class HealthcheckResultTest {

    @Test
    public void shouldCreateASuccessfulResultWithNoErrorMessage() throws Exception {

        final HealthcheckResult healthcheckResult = success();

        assertThat(healthcheckResult.isPassed(), is(true));
        assertThat(healthcheckResult.getErrorMessage(), is(empty()));
    }

    @Test
    public void shouldCreateAnErrorResultWithErrorMessage() throws Exception {

        final String errorMessage = "Oh my goodness gracious";
        final HealthcheckResult healthcheckResult = failure(errorMessage);

        assertThat(healthcheckResult.isPassed(), is(false));
        assertThat(healthcheckResult.getErrorMessage(), is(of(errorMessage)));
    }
}