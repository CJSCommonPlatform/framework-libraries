package uk.gov.justice.services.common.polling;

import uk.gov.justice.services.common.util.Sleeper;

import java.util.function.BooleanSupplier;

public class Poller {

    private final int retryCount;
    private final Sleeper sleeper;
    private final long delayIntervalMillis;

    public Poller(final int retryCount, final long delayIntervalMillis, final Sleeper sleeper) {
        this.sleeper = sleeper;
        this.retryCount = retryCount;
        this.delayIntervalMillis = delayIntervalMillis;
    }

    public boolean pollUntilTrue(final BooleanSupplier conditionalFunction) {

        for (int i = 0; i < retryCount; i++) {
            if (conditionalFunction.getAsBoolean()) {
                return true;
            }

            sleeper.sleepFor(delayIntervalMillis);
        }

        return false;
    }
}
