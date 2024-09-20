package uk.gov.moj.cpp.jobmanager.example;

import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.moj.cpp.jobstore.persistence.Priority;
import uk.gov.moj.cpp.task.execution.DefaultJobStoreSchedulerPrioritySelector;

import java.util.List;

public class BakeryJobStoreSchedulerPrioritySelector extends DefaultJobStoreSchedulerPrioritySelector {

    @Override
    public List<Priority> selectOrderedPriorities() {
        return List.of(HIGH, MEDIUM, LOW);
    }
}
