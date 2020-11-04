package uk.gov.justice.services.ejb.timer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ejb.TimerConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TimerConfigFactoryTest {

    @InjectMocks
    private TimerConfigFactory timerConfigFactory;

    @Test
    public void shouldCreateANewTimerConfig() throws Exception {

        assertThat(timerConfigFactory.createNew(), is(instanceOf(TimerConfig.class)));
    }
}
