package uk.gov.moj.cpp.jobstore.api.task;

import java.util.List;
import java.util.Optional;

public interface ExecutableTask {

    /**
     * Perform the processing for an ExecutableTask implementation
     * @param executionInfo The ExecutionInfo to be processed
     * @return updated executionInfo containing the result of the processing and possible further data
     * to be processed by a subsequent ExecutableTask (including nextStep etc)
     */
    ExecutionInfo execute(final ExecutionInfo executionInfo);

    /**
     * Implementation is optional and only required when task requires retry capability
     * @return List of retry durations in seconds
     */
    default Optional<List<Long>> getRetryDurationsInSecs() {
        return Optional.empty();
    }
}
