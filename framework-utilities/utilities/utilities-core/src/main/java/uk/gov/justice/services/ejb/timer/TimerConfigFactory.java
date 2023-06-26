package uk.gov.justice.services.ejb.timer;

import jakarta.ejb.TimerConfig;

public class TimerConfigFactory {

    public TimerConfig createNew() {
        return new TimerConfig();
    }
}
