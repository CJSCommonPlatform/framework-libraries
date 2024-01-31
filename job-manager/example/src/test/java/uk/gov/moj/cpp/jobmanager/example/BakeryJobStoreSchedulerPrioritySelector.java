package uk.gov.moj.cpp.jobmanager.example;

import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.moj.cpp.jobstore.persistence.Priority;
import uk.gov.moj.cpp.task.execution.DefaultJobStoreSchedulerPrioritySelector;

/**
 * Dummy class for integration tests to always select next jobs as HIGH priority (to make the test determinate)
 */
public class BakeryJobStoreSchedulerPrioritySelector extends DefaultJobStoreSchedulerPrioritySelector {

    @Override
    public Priority selectNextPriority() {
        return HIGH;
    }
}
