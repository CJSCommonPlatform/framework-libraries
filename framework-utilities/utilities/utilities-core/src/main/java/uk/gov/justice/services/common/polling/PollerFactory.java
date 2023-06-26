package uk.gov.justice.services.common.polling;

import uk.gov.justice.services.common.util.Sleeper;

import jakarta.inject.Inject;

public class PollerFactory {

    @Inject
    private Sleeper sleeper;

    public Poller create(
            final int pollerRetryCount,
            final long pollerDelayIntervalMillis) {

        return new Poller(pollerRetryCount, pollerDelayIntervalMillis, sleeper);
    }
}
