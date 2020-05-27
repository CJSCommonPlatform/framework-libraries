package uk.gov.justice.services.common.polling;

import uk.gov.justice.services.common.util.Sleeper;

import java.util.function.BooleanSupplier;

public class MultiIteratingPoller {

    private final Poller poller;
    private final Sleeper sleeper;
    private final int numberOfPollingIterations;
    private final long waitTimeBetweenIterationsMillis;

    public MultiIteratingPoller(
            final int numberOfPollingIterations, final long waitTimeBetweenIterationsMillis, final Poller poller,
            final Sleeper sleeper) {
        this.poller = poller;
        this.sleeper = sleeper;
        this.numberOfPollingIterations = numberOfPollingIterations;
        this.waitTimeBetweenIterationsMillis = waitTimeBetweenIterationsMillis;
    }

    public boolean pollUntilTrue(final BooleanSupplier conditionalFunction) {

        for (int i = 1; i <= numberOfPollingIterations; i++) {
            if (poller.pollUntilTrue(conditionalFunction)) {
                if(i == numberOfPollingIterations) {
                    return true;
                }
            }

            sleeper.sleepFor(waitTimeBetweenIterationsMillis);
        }
        
        return false;
    }
}
