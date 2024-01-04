package uk.gov.moj.cpp.task.extension;

import static uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo.executionInfo;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus.COMPLETED;

import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Task("sample-retry-task")
public class SampleRetryTask implements ExecutableTask {

    @Override
    public ExecutionInfo execute(final ExecutionInfo executionInfo) {
        return executionInfo().from(executionInfo)
                .withExecutionStatus(COMPLETED)
                    .build();
    }

    @Override
    public Optional<List<Long>> getRetryDurationsInSecs() {
        return Optional.of(Arrays.asList(1L, 2L));
    }
}
