package uk.gov.justice.services.common.polling;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.common.util.Sleeper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MultiIteratingPollerFactoryTest {

    @Mock
    private Sleeper sleeper;

    @Mock
    private PollerFactory pollerFactory;

    @InjectMocks
    private MultiIteratingPollerFactory multiIteratingPollerFactory;

    @Test
    public void shouldCreateMultiIteratingPollerFactory() throws Exception {

        final int pollerRetryCount = 23;
        final long pollerDelayIntervalMillis = 98723987324L;
        final int numberOfPollingIterations = 32;
        final long waitTimeBetweenIterationsMillis = 234234L;

        final Poller poller = mock(Poller.class);

        when(pollerFactory.create(pollerRetryCount, pollerDelayIntervalMillis)).thenReturn(poller);

        final MultiIteratingPoller multiIteratingPoller = multiIteratingPollerFactory.create(
                pollerRetryCount,
                pollerDelayIntervalMillis,
                numberOfPollingIterations,
                waitTimeBetweenIterationsMillis);

        assertThat(getValueOfField(multiIteratingPoller, "numberOfPollingIterations", Integer.class), is(numberOfPollingIterations));
        assertThat(getValueOfField(multiIteratingPoller, "waitTimeBetweenIterationsMillis", Long.class), is(waitTimeBetweenIterationsMillis));
        assertThat(getValueOfField(multiIteratingPoller, "poller", Poller.class), is(poller));
        assertThat(getValueOfField(multiIteratingPoller, "sleeper", Sleeper.class), is(sleeper));
    }
}
