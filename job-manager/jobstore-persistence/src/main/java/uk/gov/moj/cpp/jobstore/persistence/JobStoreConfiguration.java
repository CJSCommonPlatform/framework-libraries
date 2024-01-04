package uk.gov.moj.cpp.jobstore.persistence;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import uk.gov.justice.services.common.configuration.Value;

import javax.annotation.Resource;
import javax.inject.Inject;

public class JobStoreConfiguration {

    @Inject
    @Value(key = "jobstore.timer.start.wait.milliseconds", defaultValue = "20000")
    private String timerStartWaitMilliseconds;

    @Inject
    @Value(key = "jobstore.timer.interval.milliseconds", defaultValue = "20000")
    private String timerIntervalMilliseconds;

    /**
     * The percentage chance that the JobScheduler will select jobs with HIGH Priority
     */
    @Inject
    @Value(key = "jobstore.job.selection.percentage.high.priority", defaultValue = "70")
    private String jobPriorityPercentageHigh;

    /**
     * The percentage chance that the JobScheduler will select jobs with LOW Priority
     */
    @Inject
    @Value(key = "jobstore.job.selection.percentage.low.priority", defaultValue = "10")
    private String jobPriorityPercentageLow;

    @Inject
    @Value(key = "worker.job.count", defaultValue = "10")
    private String workerJobCount;

    @Resource(lookup = "java:module/ModuleName")
    private String moduleName;

    public long getTimerStartWaitMilliseconds() {
        return parseLong(timerStartWaitMilliseconds);
    }

    public long getTimerIntervalMilliseconds() {
        return parseLong(timerIntervalMilliseconds);
    }

    public int getJobPriorityPercentageHigh() {
        return parseInt(jobPriorityPercentageHigh);
    }

    public int getJobPriorityPercentageLow() {
        return parseInt(jobPriorityPercentageLow);
    }

    public int getWorkerJobCount() {
        return parseInt(workerJobCount);
    }

    public String getModuleName() {
        return moduleName;
    }
}
