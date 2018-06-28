package uk.gov.moj.cpp.task.extension;

import static java.util.Optional.of;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.SUCCESSFUL;

import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.persistence.Job;

@Task("sample-task")
public class SampleTask implements ExecutableTask {

    @Override
    public Job execute(final Job job) {
        return new Job(job, of(SUCCESSFUL));
    }

}
