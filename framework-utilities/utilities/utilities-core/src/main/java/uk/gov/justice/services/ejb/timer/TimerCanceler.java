package uk.gov.justice.services.ejb.timer;

import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;

public class TimerCanceler {

    public void cancelTimer(final String timerJobName, final TimerService timerService) {
        timerService.getAllTimers().stream()
                .filter(timer -> timerJobName.equals(timer.getInfo()))
                .forEach(Timer::cancel);
    }
}
