package uk.gov.moj.cpp.jobmanager.it;

import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.moj.cpp.jobstore.persistence.Priority;
import uk.gov.moj.cpp.task.execution.DefaultJobStoreSchedulerPrioritySelector;

public class DummyJobStoreSchedulerPrioritySelector extends DefaultJobStoreSchedulerPrioritySelector {

    @Override
    public Priority selectNextPriority() {
        return HIGH;
    }
}
