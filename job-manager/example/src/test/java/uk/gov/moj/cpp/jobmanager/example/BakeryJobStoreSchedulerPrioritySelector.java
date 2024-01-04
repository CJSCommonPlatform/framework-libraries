package uk.gov.moj.cpp.jobmanager.example;

import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.moj.cpp.jobstore.persistence.Priority;
import uk.gov.moj.cpp.task.execution.DefaultJobStoreSchedulerPrioritySelector;

public class BakeryJobStoreSchedulerPrioritySelector extends DefaultJobStoreSchedulerPrioritySelector {

    @Override
    public Priority selectNextPriority() {
        return HIGH;
    }
}
