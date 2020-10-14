package uk.gov.justice.services.common.polling;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.util.Sleeper;

import java.util.function.BooleanSupplier;

import javax.inject.Inject;

import org.junit.Test;

public class PollerTest {

    @Inject
    private Sleeper sleeper = mock(Sleeper.class);


    @Test
    public void shouldPollUntilTrue() throws Exception {

        final int retryCount = 5;
        final long delayIntervalMillis = 934878L;

        final Poller poller = new Poller(retryCount, delayIntervalMillis, sleeper);

        final BooleanSupplier conditionalFunction = mock(BooleanSupplier.class);

        when(conditionalFunction.getAsBoolean()).thenReturn(false, false, false, false, true);

        assertThat(poller.pollUntilTrue(conditionalFunction), is(true));

        verify(sleeper, times(4)).sleepFor(delayIntervalMillis);
    }

    @Test
    public void shouldOnlyPollUntilTrue() throws Exception {

        final int retryCount = 5;
        final long delayIntervalMillis = 934878L;

        final Poller poller = new Poller(retryCount, delayIntervalMillis, sleeper);

        final BooleanSupplier conditionalFunction = mock(BooleanSupplier.class);

        when(conditionalFunction.getAsBoolean()).thenReturn(false, true);

        assertThat(poller.pollUntilTrue(conditionalFunction), is(true));

        verify(sleeper, times(1)).sleepFor(delayIntervalMillis);
    }

    @Test
    public void shouldReturnFalseIfFalseAfterAllAttempts() throws Exception {

        final int retryCount = 5;
        final long delayIntervalMillis = 934878L;

        final Poller poller = new Poller(retryCount, delayIntervalMillis, sleeper);

        final BooleanSupplier conditionalFunction = mock(BooleanSupplier.class);
        when(conditionalFunction.getAsBoolean()).thenReturn(false, false, false, false, false);

        assertThat(poller.pollUntilTrue(conditionalFunction), is(false));

        verify(sleeper, times(5)).sleepFor(delayIntervalMillis);
    }
}
