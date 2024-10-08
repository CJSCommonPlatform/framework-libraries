package uk.gov.moj.cpp.task.execution;

import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.Priority;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class DefaultJobStoreSchedulerPrioritySelector implements JobStoreSchedulerPrioritySelector {

    @Inject
    private RandomPercentageProvider randomPercentageProvider;

    @Inject
    private JobStoreConfiguration jobStoreConfiguration;

    @Override
    public List<Priority> selectOrderedPriorities() {

        final int randomPercentage = randomPercentageProvider.getRandomPercentage();
        final int jobPriorityPercentageHigh = jobStoreConfiguration.getJobPriorityPercentageHigh();
        final int jobPriorityPercentageLow = jobStoreConfiguration.getJobPriorityPercentageLow();

        final int high = 100 - jobPriorityPercentageHigh;
        if (randomPercentage >= high) {
            return List.of(HIGH, MEDIUM, LOW);
        }

        if (randomPercentage < jobPriorityPercentageLow) {
            return List.of(LOW, HIGH, MEDIUM);
        }

        return List.of(MEDIUM, HIGH, LOW);
    }
}
