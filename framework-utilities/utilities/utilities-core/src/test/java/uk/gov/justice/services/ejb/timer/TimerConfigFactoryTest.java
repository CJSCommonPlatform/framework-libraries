package uk.gov.justice.services.ejb.timer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ejb.TimerConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimerConfigFactoryTest {

    @InjectMocks
    private TimerConfigFactory timerConfigFactory;

    @Test
    public void shouldCreateANewTimerConfig() throws Exception {

        assertThat(timerConfigFactory.createNew(), is(instanceOf(TimerConfig.class)));
    }
}
