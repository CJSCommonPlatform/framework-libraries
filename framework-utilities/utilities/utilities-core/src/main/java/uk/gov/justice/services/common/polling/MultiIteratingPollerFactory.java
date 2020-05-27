package uk.gov.justice.services.common.polling;

import uk.gov.justice.services.common.util.Sleeper;

import javax.inject.Inject;

public class MultiIteratingPollerFactory {

    @Inject
    private Sleeper sleeper;

    @Inject
    private PollerFactory pollerFactory;

    public MultiIteratingPoller create(
            final int pollerRetryCount,
            final long pollerDelayIntervalMillis,
            final int numberOfPollingIterations,
            final long waitTimeBetweenIterationsMillis) {

        final Poller poller = pollerFactory.create(pollerRetryCount, pollerDelayIntervalMillis);

        return new MultiIteratingPoller(
                numberOfPollingIterations,
                waitTimeBetweenIterationsMillis,
                poller,
                sleeper
        );
    }
}
