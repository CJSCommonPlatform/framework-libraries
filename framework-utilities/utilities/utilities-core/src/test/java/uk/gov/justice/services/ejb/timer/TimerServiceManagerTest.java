package uk.gov.justice.services.ejb.timer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.ejb.Timer;
import jakarta.ejb.TimerConfig;
import jakarta.ejb.TimerService;

import org.apache.activemq.artemis.api.core.ICoreMessage;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimerServiceManagerTest {

    @Mock
    private TimerConfigFactory timerConfigFactory;

    @InjectMocks
    private TimerServiceManager timerServiceManager;

    @Test
    public void shouldCreateAnIntervalTimer() throws Exception {
        final String timerJobName = "timer job name";
        final long timerStartWaitMilliseconds = 2384L;
        final long timerIntervalMilliseconds = 2098374L;

        final TimerService timerService = mock(TimerService.class);
        final TimerConfig timerConfig = mock(TimerConfig.class);
        final Timer timer = mock(Timer.class);

        when(timerConfigFactory.createNew()).thenReturn(timerConfig);
        when(timerService.createIntervalTimer(timerStartWaitMilliseconds, timerIntervalMilliseconds, timerConfig)).thenReturn(timer);

        assertThat(timerServiceManager.createIntervalTimer(timerJobName, timerStartWaitMilliseconds, timerIntervalMilliseconds, timerService), is(timer));

        final InOrder inOrder = inOrder(timerConfig, timerService);

        inOrder.verify(timerConfig).setPersistent(false);
        inOrder.verify(timerConfig).setInfo(timerJobName);
        inOrder.verify(timerService).createIntervalTimer(timerStartWaitMilliseconds, timerIntervalMilliseconds, timerConfig);
    }

    @Test
    public void shouldCreateASingleActionTimer() throws Exception {

        final String timerJobName = "timer job name";
        final long duration = 9839798342L;
        final TimerService timerService = mock(TimerService.class);
        final TimerConfig timerConfig = mock(TimerConfig.class);
        final Timer timer = mock(Timer.class);

        when(timerConfigFactory.createNew()).thenReturn(timerConfig);
        when(timerService.createSingleActionTimer(duration, timerConfig)).thenReturn(timer);

        assertThat(timerServiceManager.createSingleActionTimer(timerJobName, duration, timerService), is(timer));

        final InOrder inOrder = inOrder(timerConfig, timerService);

        inOrder.verify(timerConfig).setPersistent(false);
        inOrder.verify(timerConfig).setInfo(timerJobName);
        inOrder.verify(timerService).createSingleActionTimer(duration, timerConfig);
    }
}
