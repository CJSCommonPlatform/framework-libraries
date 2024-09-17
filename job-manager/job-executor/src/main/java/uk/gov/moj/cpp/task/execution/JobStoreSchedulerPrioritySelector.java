package uk.gov.moj.cpp.task.execution;

import uk.gov.moj.cpp.jobstore.persistence.Priority;

public interface JobStoreSchedulerPrioritySelector {
    Priority selectNextPriority();
}
