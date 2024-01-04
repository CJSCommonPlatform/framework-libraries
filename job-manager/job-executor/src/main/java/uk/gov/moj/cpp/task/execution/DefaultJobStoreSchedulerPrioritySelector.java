package uk.gov.moj.cpp.task.execution;

import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.Priority;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class DefaultJobStoreSchedulerPrioritySelector implements JobStoreSchedulerPrioritySelector {

    @Inject
    private RandomPercentageProvider randomPercentageProvider;

    @Inject
    private JobStoreConfiguration jobStoreConfiguration;

    @Override
    public Priority selectNextPriority() {

        final int randomPercentage = randomPercentageProvider.getRandomPercentage();
        final int jobPriorityPercentageHigh = jobStoreConfiguration.getJobPriorityPercentageHigh();
        final int jobPriorityPercentageLow = jobStoreConfiguration.getJobPriorityPercentageLow();

        final int highPriorityCutoffThreshold = 100 - jobPriorityPercentageHigh;

        if (randomPercentage >= highPriorityCutoffThreshold) {
            return HIGH;
        }

        if (randomPercentage < jobPriorityPercentageLow) {
            return LOW;
        }

        return MEDIUM;
    }
}
