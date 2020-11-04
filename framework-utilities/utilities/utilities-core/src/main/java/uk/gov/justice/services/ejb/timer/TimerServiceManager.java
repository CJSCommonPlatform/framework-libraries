package uk.gov.justice.services.ejb.timer;

import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

public class TimerServiceManager {

    @Inject
    private TimerConfigFactory timerConfigFactory;

    public void createIntervalTimer(
            final String timerJobName,
            final long timerStartWaitMilliseconds,
            final long timerIntervalMilliseconds,
            final TimerService timerService) {
        final TimerConfig timerConfig = timerConfigFactory.createNew();

        timerConfig.setPersistent(false);
        timerConfig.setInfo(timerJobName);

        timerService.createIntervalTimer(timerStartWaitMilliseconds, timerIntervalMilliseconds, timerConfig);
    }

    public void createSingleActionTimer(
            final String timerJobName,
            final long duration,
            final TimerService timerService) {
        final TimerConfig timerConfig = timerConfigFactory.createNew();

        timerConfig.setPersistent(false);
        timerConfig.setInfo(timerJobName);

        timerService.createSingleActionTimer(duration, timerConfig);
    }
}
