package uk.gov.moj.cpp.task.execution;

import uk.gov.moj.cpp.jobstore.persistence.Priority;

import java.util.List;

public interface JobStoreSchedulerPrioritySelector {
    List<Priority> selectOrderedPriorities();
}
