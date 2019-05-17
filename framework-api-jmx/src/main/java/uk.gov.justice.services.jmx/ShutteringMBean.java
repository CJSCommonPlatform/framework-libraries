package uk.gov.justice.services.jmx;

import javax.management.MXBean;

@MXBean
public interface ShutteringMBean {

    void doShutteringRequested();

    void doUnshutteringRequested();
}
