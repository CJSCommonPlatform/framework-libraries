package uk.gov.justice.services.common.polling;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.common.util.Sleeper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
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
