package uk.gov.moj.cpp.jobstore.api.task;

import uk.gov.moj.cpp.jobstore.persistence.Job;

public interface ExecutableTask {

    /**
     * Perform the processing for an ExecutableTask implementation
     * @param job The Job to be processed
     * @return A Job containing the result of the processing and possible further data
     * to be processed by a subsequent ExecutableTask (including nextStep etc)
     */
    Job execute(final Job job);

}
