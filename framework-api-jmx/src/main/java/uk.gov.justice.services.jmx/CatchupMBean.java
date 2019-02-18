package uk.gov.justice.services.jmx;

import javax.management.MXBean;

@MXBean
public interface CatchupMBean {
    void doCatchupRequested();
}
