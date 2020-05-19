package uk.gov.justice.services.common.util;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Wrapper for Thread.sleep() to allow mocking
 */
public class Sleeper {

    /**
     * Sleeps for the specified number of milliseconds
     * @param milliseconds the duration of the sleep in milliseconds
     */
    public void sleepFor(final long milliseconds) {
        try {
            sleep(milliseconds);
        } catch (final InterruptedException unused) {
            currentThread().interrupt();
        }
    }
}
