package uk.gov.moj.cpp.task.execution;

import uk.gov.moj.cpp.jobstore.persistence.Priority;

/**
 * Selects the priority of the next job(s) to run by the job scheduler worker threads. These
 * can be HIGH, MEDIUM or LOW Priority. These are selected randomly with (by default)
 *
 * 70% chance of HIGH Priority
 * 10% chance of LOW Priority
 * the remaining 20% chance of MEDIUM Priority
 *
 * These thresholds are set as JNDI values {@link uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration}
 */
public interface JobStoreSchedulerPrioritySelector {
    Priority selectNextPriority();
}
