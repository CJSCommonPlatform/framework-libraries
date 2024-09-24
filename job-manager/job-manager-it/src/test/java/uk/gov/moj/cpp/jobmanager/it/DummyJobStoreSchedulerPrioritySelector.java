package uk.gov.moj.cpp.jobmanager.it;

import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.moj.cpp.jobstore.persistence.Priority;
import uk.gov.moj.cpp.task.execution.DefaultJobStoreSchedulerPrioritySelector;

import java.util.List;

public class DummyJobStoreSchedulerPrioritySelector extends DefaultJobStoreSchedulerPrioritySelector {

    @Override
    public List<Priority> selectOrderedPriorities() {
        return List.of(HIGH, MEDIUM, LOW);
    }
}
