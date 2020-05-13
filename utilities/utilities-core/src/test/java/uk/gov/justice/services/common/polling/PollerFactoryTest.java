package uk.gov.justice.services.common.polling;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.common.util.Sleeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PollerFactoryTest {

    @Mock
    private Sleeper sleeper;

    @InjectMocks
    private PollerFactory pollerFactory;

    @Test
    public void shouldCreateAPoller() throws Exception {

        final int pollerRetryCount = 23;
        final long pollerDelayIntervalMillis = 912873L;

        final Poller poller = pollerFactory.create(pollerRetryCount, pollerDelayIntervalMillis);

        assertThat(getValueOfField(poller, "retryCount", Integer.class), is(pollerRetryCount));
        assertThat(getValueOfField(poller, "sleeper", Sleeper.class), is(sleeper));
        assertThat(getValueOfField(poller, "delayIntervalMillis", Long.class), is(pollerDelayIntervalMillis));
    }
}
